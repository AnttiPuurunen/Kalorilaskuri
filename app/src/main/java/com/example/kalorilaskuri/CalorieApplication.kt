package com.example.kalorilaskuri

import android.app.Application
import com.example.kalorilaskuri.database.AppDatabase

// Määrittää käytettävän tietokannan ja lataa sen vasta tarvittaessa

class CalorieApplication: Application() {
    val database: AppDatabase by lazy {AppDatabase.getDatabase(this)}
}