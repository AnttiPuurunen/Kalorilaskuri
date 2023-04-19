package com.example.kalorilaskuri.database.caloriecalc

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Dataluokka, joka vastaa taulua tietokannassa, jossa syödyn aterian tiedot

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val mealId: Int = 0,
    @NonNull @ColumnInfo(name = "meal_date") val mealDate: String,
    @NonNull @ColumnInfo(name = "food_name") val foodName: String,
    @NonNull @ColumnInfo(name= "quantity") val quantity: Int,
    @NonNull @ColumnInfo(name= "calories_amount") val caloriesAmount: Int,
    @NonNull @ColumnInfo(name = "calories") val calories: Int
)