package com.github.utn.frba.mobile.dextracker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.utn.frba.mobile.dextracker.async.AsyncCoroutineExecutor
import com.github.utn.frba.mobile.dextracker.constants.REDIRECT
import com.github.utn.frba.mobile.dextracker.db.storage.SessionStorage
import com.github.utn.frba.mobile.dextracker.extensions.replaceWithAnimWith
import com.github.utn.frba.mobile.dextracker.firebase.Redirection
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import com.github.utn.frba.mobile.dextracker.utils.objectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator

const val cameraRequestCode = 9999

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val perfilFragment = PerfilFragment()
    private val myDexFragment = MyDexFragment()
    private val favDexFragment = FavDEX_Fragment()
    private val favPokesFragment = FavPokes_Fragment()
    private var urlScan: String = ""
    private var userId: String = ""
    private var dexId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DexTracker_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            /*if(Permisos.checkForPermissions(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    cameraRequestCode,
                    "No se puede escanear un codigo si no tenemos acceso a tu camara"))
                    {*/
            val scanner = IntentIntegrator(this)
            scanner.setOrientationLocked(false)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setPrompt("Scan the Pokedex!")
            scanner.initiateScan()
            //}

        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false
        bottomNavigationView.selectedItemId = R.id.misdex
        val redirect: Redirection? = this.intent.extras?.getString(REDIRECT)
            ?.let { objectMapper.readValue(it) }

        val fragment = redirect?.also { Log.i(TAG, "Redirect to ${it.location()}") }
            ?.to()
            ?: myDexFragment
        makeCurrentFragment(fragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.perfil -> makeCurrentFragment(perfilFragment)
                R.id.misdex -> makeCurrentFragment(myDexFragment)
                R.id.favdex -> makeCurrentFragment(favDexFragment)
                R.id.favpokes -> makeCurrentFragment(favPokesFragment)
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled :(", Toast.LENGTH_LONG).show()
                } else {
                    urlScan = result.contents
                    val urlExtract = urlScan.substringAfter("https://dex-tracker.herokuapp.com/users/")
                    userId = urlExtract.substringBefore("/dex/")
                    dexId = urlExtract.substringAfter("/dex/")
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            cameraRequestCode -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val scanner = IntentIntegrator(this)
                    scanner.setOrientationLocked(false)
                    scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                    scanner.setPrompt("Scan the Pokedex!")
                    scanner.initiateScan()
                } else {
                    Toast.makeText(this, "You didn't give me permission D:", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_open_enter,
                    R.anim.fragment_fade_exit,
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_open_exit,
                )
            replace(R.id.fl_wrapper, fragment)
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        val sessionStorage = SessionStorage(this)
        AsyncCoroutineExecutor.dispatch { sessionStorage.store(inMemoryRepository.session) }
    }

    override fun onResume() {
        super.onResume()
        if(urlScan != ""){
            //Toast.makeText(this,"Scanned: $urlScan",Toast.LENGTH_LONG).show()//DEBUG ONLY
            bottomNavigationView.selectedItemId = R.id.favdex
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_open_enter,
                    R.anim.fragment_fade_exit,
                    R.anim.fragment_open_enter,
                    R.anim.fragment_open_exit,
                )
                replace(
                    R.id.fl_wrapper,
                    PokedexFragment.newInstance(userId = userId,dexId = dexId)
                )
            }
            userId = ""
            dexId = ""
            urlScan = ""
        }
    }

    companion object {
        private const val TAG = "MAIN"
    }
}
