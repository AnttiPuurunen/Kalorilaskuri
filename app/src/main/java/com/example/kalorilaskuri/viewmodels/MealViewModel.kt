package com.example.kalorilaskuri.viewmodels

import androidx.lifecycle.*
import com.example.kalorilaskuri.database.caloriecalc.Meal
import com.example.kalorilaskuri.database.caloriecalc.MealDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Viewmodel tiedonkäsittelyyn, tämän kautta kutsutaan MealDao-interfacen metodeja

class MealViewModel(private val mealDao: MealDao): ViewModel() {
    private val selectedMeal: MutableLiveData<Meal> = MutableLiveData()
    // Hakee listaan kaikki tietokannan tiedot
    val allItems: LiveData<List<Meal>> = mealDao.getAllMeals().asLiveData()


    fun getAllMeals(): Flow<List<Meal>>
            = mealDao.getAllMeals()

    fun getNumberOfMeals(): Int
            = mealDao.getNumberOfMeals()

    // Otetaan vastaan uuden aterian tiedot ja haetaan uusi Meal-objekti. Tätä kutsutaan fragmentista.
    fun addNewMeal(mealDate: String, foodName: String, quantity: Int, caloriesAmount: Int, calories: String) {
        val newMeal = getNewMealEntry(mealDate, foodName, quantity, caloriesAmount, calories)
        insertMeal(newMeal)
    }

    // Uuden aterian lisääminen tietokantaan, käynnistetään coroutinessa.
    private fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDao.insert(meal)
        }
    }

    // Palauttaa uuden Meal-objektin käyttäjän antamilla tiedoilla.
    private fun getNewMealEntry(mealDate: String, foodName: String, quantity: Int, caloriesAmount: Int, calories: String): Meal {
        return Meal(
            mealDate = mealDate,
            foodName = foodName,
            quantity = quantity,
            caloriesAmount = caloriesAmount,
            calories = calories
        )
    }
    fun setSelectedMeal(meal: Meal) {
        selectedMeal.value = meal
    }

    fun getSelectedMeal(): LiveData<Meal> {
        return selectedMeal
    }
}

class MealViewModelFactory(
    private val mealDao: MealDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(mealDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}