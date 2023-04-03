package com.example.kalorilaskuri.database.caloriecalc

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Data Access Object-interface, jolla päästään käsiksi tietokannan tauluihin

@Dao
interface MealDao {

    @Query("SELECT * FROM meal ORDER BY mealId ASC")
    fun getAllMeals(): Flow<List<Meal>>

    @Query("SELECT COUNT(*) FROM meal")
    fun getNumberOfMeals(): Int

    @Query("SELECT * FROM meal WHERE meal_date = :mealDate ORDER BY mealId ASC")
    fun getMealsByDate(mealDate: String): Flow<List<Meal>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal)
}