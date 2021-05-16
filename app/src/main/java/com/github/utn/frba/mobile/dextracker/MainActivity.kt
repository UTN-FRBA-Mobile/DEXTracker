package com.github.utn.frba.mobile.dextracker

//import com.google.android.material.snackbar.Snackbar

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator

const val cameraRequestCode = 9999

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        //Thread.sleep(2000)  //Solo lo use para testear el splash
        setTheme(R.style.Theme_DexTracker_NoActionBar)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            //Toast.makeText(this,"Aqui pondria el scanner...si tan solo tuviera uno",Toast.LENGTH_LONG).show()
            /*if(Permisos.checkForPermissions(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    cameraRequestCode,
                    "No se puede escanear un codigo si no tenemos acceso a tu camara"))
                    {*/
                    val scanner = IntentIntegrator(this)
                    scanner.setOrientationLocked(false)
                    scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                    scanner.setPrompt("Escanea el codigo QR de la PokeDEX deseada")
                    scanner.initiateScan()
            //}

            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false
        bottomNavigationView.selectedItemId = R.id.misdex

        //CON FRAGMENTS
        val perfilFragment = PerfilFragment()
        val misDexFragment = MisDexFragment()
        val favDexFragment = FavDexFragment()
        val favPokesFragment = FavPokesFragment()
        makeCurrentFragment(misDexFragment)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.perfil -> makeCurrentFragment(perfilFragment)
                R.id.misdex -> makeCurrentFragment(misDexFragment)
                R.id.favdex -> makeCurrentFragment(favDexFragment)
                R.id.favpokes -> makeCurrentFragment(favPokesFragment)
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelado :(", Toast.LENGTH_LONG).show()
                } else {
                    //Aca iria la redireccion hacia la DEX
                    Toast.makeText(this, "Escaneado: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            cameraRequestCode -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val scanner = IntentIntegrator(this)
                    scanner.setOrientationLocked(false)
                    scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                    scanner.setPrompt("Escanea el codigo QR de la PokeDEX deseada")
                    scanner.initiateScan()
                }
                else {
                    Toast.makeText(this, "No me diste permiso!", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}