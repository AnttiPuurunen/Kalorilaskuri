package com.example.kalorilaskuri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        // Alla oleva oli tilapaisratkaisu tietokanta-tiedoston tuhoamiseen
        deleteDatabase("calorie_database")
    }

    override fun onSupportNavigateUp(): Boolean {
        // Toisin kuin oppaassa sanottiin, navControllerin alustus onCreate-metodissa ei riitä,
        // jos sitä ei alusta muualla, sovellus kaatuu, jos up-nappia painaa, koska navController on alustamaton
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}