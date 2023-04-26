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
    private val onItemClicked: (MealExpanded) -> Unit
) : ListAdapter<MealExpanded, MealAdapter.MealExpandedViewHolder>(DiffCallback) {
    private var kalorilimit: Float = 0f

    fun setKaloriLimit(limit: Float) {
        kalorilimit = limit
    }
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
        holder.bind(getItem(position), kalorilimit)
    }

    class MealExpandedViewHolder(private var binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var expand = false
        // Bindataan näytettävän listan kentät käyttöliittymässä Meal-dataluokan properteihin
        fun bind(meal: MealExpanded, kalorilimit: Float) {
            binding.apply {
                mealDate.text = meal.date
                countOfMeals.text = meal.mealsList.size.toString()
                totalCalories.text = meal.totalCal.toString()
                if (meal.totalCal > kalorilimit) {

                        binding.totalCalories.setText("Kalorit ylittyneet")
                    }
                //recyclerViewMealsByDate.layoutManager = LinearLayoutManager()
                val mealsByDateAdapter = MealsByDateAdapter {

                }

                recyclerViewMealsByDate.adapter = mealsByDateAdapter
                recyclerViewMealsByDate.visibility = View.GONE
                emptyListTextview.visibility = View.GONE

                itemView.setOnClickListener {
                    expand = !expand

                    if (meal.mealsList.isEmpty() && expand){
                        recyclerViewMealsByDate.visibility = View.GONE
                        emptyListTextview.visibility = View.VISIBLE
                    } else if (meal.mealsList.isNotEmpty() && expand){
                        mealsByDateAdapter.submitList(meal.mealsList)
                        recyclerViewMealsByDate.visibility = View.VISIBLE
                        emptyListTextview.visibility = View.GONE
                    } else {
                        recyclerViewMealsByDate.visibility = View.GONE
                        emptyListTextview.visibility = View.GONE
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