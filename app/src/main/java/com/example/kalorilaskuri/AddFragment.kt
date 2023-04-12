package com.example.kalorilaskuri

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.kalorilaskuri.databinding.FragmentAddBinding
import java.text.SimpleDateFormat
import java.util.*
import com.example.kalorilaskuri.viewmodels.MealViewModel
import com.example.kalorilaskuri.viewmodels.MealViewModelFactory

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private val mealViewModel: MealViewModel by activityViewModels {
        MealViewModelFactory(
            (activity?.application as CalorieApplication).database.mealDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.maaraSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val maaraText = "$progress g"
                binding.maaraSeekBar1.text = maaraText
                laskeKalorit()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.kalorimaaraeditTextNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                laskeKalorit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, mutableListOf("Valitse"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        mealViewModel.allItems.observe(viewLifecycleOwner) { foods ->
            adapter.addAll(foods.map { "${it.foodName} ${it.caloriesAmount}" })
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    binding.ruokaEditText.setText("")
                    binding.kalorimaaraeditTextNumber.setText("")
                } else {
                    val meal = mealViewModel.allItems.value?.get(position - 1)
                    binding.ruokaEditText.setText(meal?.foodName)
                    binding.kalorimaaraeditTextNumber.setText(meal?.caloriesAmount?.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        binding.tallennaButton.setOnClickListener {
            val foodName = binding.ruokaEditText.text.toString()
            val quantity = binding.maaraSeekBar.progress
            val caloriesamount = binding.kalorimaaraeditTextNumber.text.toString().toInt()
            val calories = binding.kaloritextView.text.toString()
            val mealDate = getCurrentDate()

            mealViewModel.addNewMeal(mealDate = mealDate, foodName = foodName, quantity = quantity, caloriesAmount = caloriesamount, calories = calories)

        }

        return view
    }
    private fun laskeKalorit() {
        val maara = binding.maaraSeekBar.progress
        val arvo = binding.kalorimaaraeditTextNumber.text.toString().toDoubleOrNull() ?: 0.0
        val tulos = maara * arvo / 100
        val tulosText = "$tulos kcal"
        binding.kaloritextView.text = tulosText
    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}