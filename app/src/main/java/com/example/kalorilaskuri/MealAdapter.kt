package com.example.kalorilaskuri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kalorilaskuri.database.caloriecalc.MealExpanded
import com.example.kalorilaskuri.databinding.MealItemBinding

// Adapteri (DetailsFragmentissa olevalle) RecyclerViewille, käsittelee datan näytettäväksi käyttöliittymässä

class MealAdapter(
    private val onItemClicked: (MealExpanded) -> Unit,
    private val kaloriLimit: Int
) : ListAdapter<MealExpanded, MealAdapter.MealExpandedViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealExpandedViewHolder {
        val viewHolder = MealExpandedViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MealExpandedViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal, kaloriLimit)
    }

    class MealExpandedViewHolder(private var binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var expand = false
        // Bindataan näytettävän listan kentät käyttöliittymässä Meal-dataluokan properteihin
        fun bind(meal: MealExpanded, kaloriLimit: Int) {
            binding.apply {
                mealDate.text = meal.date
                countOfMeals.text = meal.mealsList.size.toString()
                totalCalories.text = meal.totalCal.toString()
                if (meal.totalCal > kaloriLimit) {

                        binding.totalCalories.setText("Päivän kaloriraja " + kaloriLimit + "/"+ meal.totalCal)
                    }
                //recyclerViewMealsByDate.layoutManager = LinearLayoutManager()
                val mealsByDateAdapter = MealsByDateAdapter {

                }

                recyclerViewMealsByDate.adapter = mealsByDateAdapter
                recyclerViewMealsByDate.visibility = View.GONE

                itemView.setOnClickListener {
                    expand = !expand

                    if (meal.mealsList.isEmpty() && expand){
                        recyclerViewMealsByDate.visibility = View.GONE
                    } else if (meal.mealsList.isNotEmpty() && expand){
                        mealsByDateAdapter.submitList(meal.mealsList)
                        recyclerViewMealsByDate.visibility = View.VISIBLE
                    } else {
                        recyclerViewMealsByDate.visibility = View.GONE
                    }

                }
            }

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MealExpanded>() {
            override fun areItemsTheSame(oldItem: MealExpanded, newItem: MealExpanded): Boolean {
                return oldItem.mealsList == newItem.mealsList
            }

            override fun areContentsTheSame(oldItem: MealExpanded, newItem: MealExpanded): Boolean {
                return oldItem == newItem
            }
        }
    }
}