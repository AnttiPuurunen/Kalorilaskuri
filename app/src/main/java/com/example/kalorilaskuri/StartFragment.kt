package com.example.kalorilaskuri

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kalorilaskuri.databinding.FragmentStartBinding
import com.example.kalorilaskuri.viewmodels.MealViewModel
import com.example.kalorilaskuri.viewmodels.MealViewModelFactory

class StartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val mealViewModel: MealViewModel by activityViewModels {
        MealViewModelFactory(
            (activity?.application as CalorieApplication).database.mealDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            startFragment = this@StartFragment
        }


        // Bindataan funktio Lisää dataa-nappiin käyttöliittymässä
        binding.addFillerData.setOnClickListener {
            //addFillerData()
        }
    }

/**    private fun addFillerData() {
        // Filleridataa testausta varten

        mealViewModel.addNewMeal("20.12.2023", "Kakku", 3)
        mealViewModel.addNewMeal("01.10.2022", "Porkkana", 7)
        mealViewModel.addNewMeal("20.02.2021", "Kana", 2)

    }*/
    fun goToAddScreen() {
        findNavController().navigate(R.id.action_startFragment_to_addFragment2)
    }

    fun goToDetailsScreen() {
        findNavController().navigate(R.id.action_startFragment_to_detailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}