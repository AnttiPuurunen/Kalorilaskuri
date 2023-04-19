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
import android.widget.Toast



class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val mealViewModel: MealViewModel by activityViewModels {
        MealViewModelFactory(
            (activity?.application as CalorieApplication).database.mealDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
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

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val defaultItems = listOf("Valitse", "Liha 300", "Kala 250", "Kana 200")
        adapter.addAll(defaultItems)
        binding.spinner.adapter = adapter

        mealViewModel.allItems.observe(viewLifecycleOwner) { foods ->
            adapter.clear()
            adapter.addAll(defaultItems)
            val foodItems = foods.map { "${it.foodName} ${it.caloriesAmount}" }.distinct()
            adapter.addAll(foodItems.filterNot { defaultItems.contains(it) })
        }

        binding.spinner.setSelection(0)

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    if (binding.kalorimaaraeditTextNumber.text.toString() != "") {
                        binding.kalorimaaraeditTextNumber.setText("")
                    }
                    if (binding.ruokaEditText.text.toString() != "") {
                        binding.ruokaEditText.setText("")
                    }
                } else if (position <= 3) {
                    val defaultValues = parent.getItemAtPosition(position).toString().split(" ")
                    binding.ruokaEditText.setText(defaultValues[0])
                    binding.kalorimaaraeditTextNumber.setText(defaultValues[1])
                } else {
                    // Set values from the selected meal item
                    val meal = mealViewModel.allItems.value?.get(position - 4)
                    binding.ruokaEditText.setText(meal?.foodName)
                    binding.kalorimaaraeditTextNumber.setText(meal?.caloriesAmount?.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }.also { binding.spinner.onItemSelectedListener = it }





        binding.tallennaButton.setOnClickListener {
            val foodName = binding.ruokaEditText.text.toString()
            val quantity = binding.maaraSeekBar.progress
            val caloriesamount = binding.kalorimaaraeditTextNumber.text.toString()
            val calories = calculatedCalories
            val mealDate = getCurrentDate()

            if (foodName.isBlank() || caloriesamount.isBlank()) {
                Toast.makeText(requireContext(), "Täytä kaikki kentät", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mealViewModel.addNewMeal(mealDate = mealDate, foodName = foodName, quantity = quantity, caloriesAmount = caloriesamount.toInt(), calories = calories)
            Toast.makeText(requireContext(), "Ruoka lisätty!", Toast.LENGTH_SHORT).show()
        }



        return view
    }
    private var calculatedCalories = 0
    private fun laskeKalorit() {
        val maara = binding.maaraSeekBar.progress
        val arvoString = binding.kalorimaaraeditTextNumber.text.toString()

        if (arvoString.isEmpty()) {
            binding.kaloritextView.text = "0 kcal"
            return
        }

        val arvo = arvoString.toInt()
        calculatedCalories = maara * arvo / 100
        val tulosText = "$calculatedCalories kcal"
        binding.kaloritextView.text = tulosText
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}