package com.example.kalorilaskuri

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.kalorilaskuri.databinding.FragmentDetailsBinding
import com.example.kalorilaskuri.viewmodels.MealViewModel
import com.example.kalorilaskuri.viewmodels.MealViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kalorilaskuri.database.caloriecalc.MealExpanded


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val mealViewModel: MealViewModel by activityViewModels {
        MealViewModelFactory(
            (activity?.application as CalorieApplication).database.mealDao()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            var detailsFragment = this@DetailsFragment
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MealAdapter {
        }
        binding.recyclerView.adapter = adapter

        // Tarkkailee viewmodelin mealsByDate-listaa, joka sisältää muokattuja MealExpanded-luokan objekteja ja päivittää tiedot eteenpäin recyclerviewin adapterille
        mealViewModel.mealsByDate.observe(this.viewLifecycleOwner) { items ->
            items.let {
                val reverseList = it.reversed()
                adapter.submitList(reverseList.distinctBy { it.date })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}