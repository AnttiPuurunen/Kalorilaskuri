package com.example.kalorilaskuri.database.caloriecalc
// Apudataluokka, jolla saadaan kerättyä ja näytettyä lisätietoa
data class MealExpanded(
    val date: String,
    val totalCal: Int,
    val mealsList: List<Meal>
)