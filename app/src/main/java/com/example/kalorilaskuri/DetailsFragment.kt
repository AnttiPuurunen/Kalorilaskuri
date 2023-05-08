package com.example.kalorilaskuri

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kalorilaskuri.database.caloriecalc.MealExpanded
import com.example.kalorilaskuri.databinding.FragmentDetailsBinding
import com.example.kalorilaskuri.viewmodels.MealViewModel
import com.example.kalorilaskuri.viewmodels.MealViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import android.content.Context

class DetailsFragment : Fragment() {

    private val prefsname = "MyPrefs"
    private val kalorilimitkey = "KaloriLimit"

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
        var chosenDate = ""
        var showCalendar = false
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            var detailsFragment = this@DetailsFragment
        }

        val prefs = requireActivity().getSharedPreferences(prefsname, Context.MODE_PRIVATE)
        val kaloriLimit = prefs.getInt(kalorilimitkey, 2000)


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MealAdapter(
            onItemClicked = { meal: MealExpanded ->

            },
            kaloriLimit = kaloriLimit
        )

        binding.recyclerView.adapter = adapter

        binding.calendarView.apply {
            visibility = View.GONE
            maxDate = System.currentTimeMillis()
        }

        binding.showCalendar.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.baseline_arrow_drop_down_24_white,
            0,
            0,
            0
        )

        binding.showCalendar.setOnClickListener {
            showCalendar = !showCalendar
            binding.calendarView.visibility =
                if (showCalendar) View.VISIBLE else View.GONE
            if (!showCalendar) {
                binding.showCalendar.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_arrow_drop_down_24_white,
                    0,
                    0,
                    0
                )
            } else {
                binding.showCalendar.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_arrow_drop_up_24_white,
                    0,
                    0,
                    0
                )
            }
        }

        binding.chosenDateChip.visibility = View.GONE
        binding.emptyListTextview.visibility = View.GONE

        binding.calendarView
            .setOnDateChangeListener{ _, year, month, dayOfMonth ->
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val c = Calendar.getInstance()
                    c.set(year, month, dayOfMonth)
                    chosenDate = dateFormat.format(c.timeInMillis)
                    observeViewModelAgain(adapter, chosenDate)
                    binding.chosenDateChip.apply {
                        text = chosenDate
                        visibility = View.VISIBLE
                    }
                }

        observeViewModelAgain(adapter, chosenDate)

        binding.chosenDateChip.setOnClickListener {
            if (chosenDate != "") {
                chosenDate = ""
                observeViewModelAgain(adapter, chosenDate)
                binding.chosenDateChip.visibility = View.GONE
            }
        }
    }
    // Tarkkailee viewmodelin mealsByDate-listaa, joka sisältää muokattuja MealExpanded-luokan objekteja ja päivittää tiedot eteenpäin recyclerviewin adapterille
    private fun observeViewModelAgain(adapter: MealAdapter, chosenDate: String) {
        mealViewModel.mealsByDate.removeObservers(this)
        mealViewModel.mealsByDate.observe(this.viewLifecycleOwner) { items ->
            items.let {
                if (it.isEmpty()) {
                    binding.apply {
                        emptyListTextview.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                } else if (chosenDate == "") {
                    adapter.submitList(it.distinctBy { it.date })
                    binding.apply {
                        emptyListTextview.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                } else {
                    val list = it.filter { it.date == chosenDate }
                    adapter.submitList(list.distinctBy { it.date })
                    binding.apply {
                        emptyListTextview.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}