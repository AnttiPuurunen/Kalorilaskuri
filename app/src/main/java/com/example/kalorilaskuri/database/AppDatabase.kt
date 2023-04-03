package com.example.kalorilaskuri.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kalorilaskuri.database.caloriecalc.*

// Määrittää ja hakee tai tarvittaessa luo uuden tietokanta-instanssin.

@Database(entities = [Meal::class], version = 6, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "calorie_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}