package com.example.kalorilaskuri

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kalorilaskuri.databinding.FragmentStartBinding
import android.content.Context

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val sharedPreferences by lazy { requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE) }
    private var savedValue = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        savedValue = sharedPreferences.getFloat("kaloriLimit", 0f)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            startFragment = this@StartFragment
            kaloriLimit.setText(savedValue.toString())
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val editor = sharedPreferences.edit()
        editor.putFloat("kaloriLimit", binding.kaloriLimit.text.toString().toFloatOrNull() ?: 0f)
        editor.apply()
    }

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