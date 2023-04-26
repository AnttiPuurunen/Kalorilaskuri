package com.example.kalorilaskuri

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kalorilaskuri.database.caloriecalc.Meal
import com.example.kalorilaskuri.databinding.MealItemBinding
import com.example.kalorilaskuri.databinding.MealsByDateItemBinding

class MealsByDateAdapter(
    private val onItemClicked: (Meal) -> Unit
) : ListAdapter<Meal, MealsByDateAdapter.MealsByDateViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsByDateViewHolder {
        val viewHolder = MealsByDateViewHolder(
            MealsByDateItemBinding.inflate(
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

    override fun onBindViewHolder(holder: MealsByDateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MealsByDateViewHolder(private var binding: MealsByDateItemBinding): RecyclerView.ViewHolder(binding.root) {

        // Bindataan näytettävän listan kentät käyttöliittymässä Meal-dataluokan properteihin
        fun bind(meal: Meal) {
            binding.apply {
                mealName.text = meal.foodName
                calories.text = meal.calories.toString() + itemView.context.getString(R.string.total_calories)
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