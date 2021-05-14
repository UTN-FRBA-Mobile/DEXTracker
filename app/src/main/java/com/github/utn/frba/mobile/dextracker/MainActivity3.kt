package com.github.utn.frba.mobile.dextracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.pantalla3
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantalla2 ->{
                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                }
                R.id.pantalla1 ->{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.pantalla4 ->{
                    val intent = Intent(this, MainActivity4::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
}