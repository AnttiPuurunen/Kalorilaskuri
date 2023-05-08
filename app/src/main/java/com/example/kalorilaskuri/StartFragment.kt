package com.example.kalorilaskuri

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kalorilaskuri.databinding.FragmentStartBinding
import android.content.Context
import android.widget.EditText
import android.content.SharedPreferences
import android.text.TextWatcher
import android.text.Editable

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val PREFS_NAME = "MyPrefs"
    private val KALORI_LIMIT_KEY = "KaloriLimit"

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPrefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()

        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            startFragment = this@StartFragment
        }
        val kaloriLimitEditText = view.findViewById<EditText>(R.id.kaloriLimit)
        kaloriLimitEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val kaloriLimit = s.toString().toIntOrNull()
                if (kaloriLimit != null) {
                    editor.putInt(KALORI_LIMIT_KEY, kaloriLimit)
                    editor.apply()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })
        val savedKaloriLimit = sharedPrefs.getInt(KALORI_LIMIT_KEY, -1)
        if (savedKaloriLimit != -1) {
            kaloriLimitEditText.setText(savedKaloriLimit.toString())
        }
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