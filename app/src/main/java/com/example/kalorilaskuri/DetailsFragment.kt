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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DetailsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
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

        // Tarkkailee viewmodelin allItems-listaa, johon haetaan tietokannan tiedot ja päivittää tiedot eteenpäin recyclerviewin adapterille
        mealViewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}