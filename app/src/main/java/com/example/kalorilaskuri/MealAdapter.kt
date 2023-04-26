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
        holder.bind(getItem(position))
    }

    class MealExpandedViewHolder(private var binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var expand = false
        // Bindataan näytettävän listan kentät käyttöliittymässä Meal-dataluokan properteihin
        fun bind(meal: MealExpanded) {
            binding.apply {
                mealDate.text = meal.date
                countOfMeals.text = meal.mealsList.size.toString()
                totalCalories.text = meal.totalCal.toString()
                if (meal.totalCal > 2000) {

                        binding.totalCalories.setText("Kalorit ylittyneet")
                    }
                //recyclerViewMealsByDate.layoutManager = LinearLayoutManager()
                val mealsByDateAdapter = MealsByDateAdapter {

                }
                recyclerViewMealsByDate.adapter = mealsByDateAdapter
                recyclerViewMealsByDate.visibility = View.GONE
                mealsByDateAdapter.submitList(meal.mealsList)
                itemView.setOnClickListener {
                    expand = !expand
                    recyclerViewMealsByDate.visibility =
                        if (expand) View.VISIBLE else View.GONE
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