package com.example.kalorilaskuri

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kalorilaskuri.database.caloriecalc.Meal
import com.example.kalorilaskuri.databinding.MealItemBinding

// Adapteri (StartFragmentissa olevalle) RecyclerViewille, käsittelee datan näytettäväksi käyttöliittymässä

class MealAdapter(
    private val onItemClicked: (Meal) -> Unit
) : ListAdapter<Meal, MealAdapter.MealViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val viewHolder = MealViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MealViewHolder(private var binding: MealItemBinding): RecyclerView.ViewHolder(binding.root) {

        // Bindataan näytettävän listan kentät käyttöliittymässä Meal-dataluokan properteihin
        fun bind(meal: Meal) {
            binding.apply {
                mealDate.text = meal.mealDate
                foodName.text = meal.foodName
                foodQuantity.text = meal.quantity.toString()
            }

        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Meal>() {
            override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem.mealId == newItem.mealId
            }

            override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem == newItem
            }
        }
    }
}