package com.example.kalorilaskuri

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {



    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        val locale = Locale("fi")
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )

        // Alla oleva oli tilapaisratkaisu tietokanta-tiedoston tuhoamiseen
        //deleteDatabase("calorie_database")
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