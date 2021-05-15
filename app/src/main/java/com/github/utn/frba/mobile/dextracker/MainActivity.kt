package com.github.utn.frba.mobile.dextracker

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.android.material.snackbar.Snackbar
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        //Thread.sleep(2000)  //Solo lo use para testear el splash
        setTheme(R.style.Theme_DexTracker_NoActionBar)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Toast.makeText(this, "Aqui pondria el scanner...si tan solo tuviera uno", Toast.LENGTH_LONG).show()
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
        }

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false
        bottomNavigationView.selectedItemId = R.id.misdex
      /*//CON ACTIVITIES
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantalla2 ->{
                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                }
                R.id.pantalla3 ->{
                    val intent = Intent(this, MainActivity3::class.java)
                    startActivity(intent)
                }
                R.id.pantalla4 ->{
                    val intent = Intent(this, MainActivity4::class.java)
                    startActivity(intent)
                }
            }
            true
        }*/
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