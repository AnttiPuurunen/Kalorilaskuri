package com.example.kalorilaskuri

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher

data class Ruoka(val nimi: String, val maara: Int, val kalorit: String)

class AddFragment : Fragment() {
    /**private lateinit var ruokaViewModel: RuokaViewModel*/
    private lateinit var maaraSeekBar1: TextView
    private lateinit var maaraSeekBar: SeekBar
    private lateinit var ruokaEditText: TextView
    private lateinit var tallennaButton: Button
    private lateinit var kaloritextView: TextView
    private lateinit var kalorimaaraeditTextNumber: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        maaraSeekBar1 = view.findViewById(R.id.maaraSeekBar1)
        maaraSeekBar = view.findViewById(R.id.maaraSeekBar)
        ruokaEditText = view.findViewById(R.id.ruokaEditText)
        tallennaButton = view.findViewById(R.id.tallennaButton)
        kaloritextView = view.findViewById(R.id.kaloritextView)
        kalorimaaraeditTextNumber = view.findViewById(R.id.kalorimaaraeditTextNumber)
        //ruokaViewModel = ViewModelProvider(this).get(RuokaViewModel::class.java)

        maaraSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val maaraText = "$progress g"
                maaraSeekBar1.text = maaraText
                laskeKalorit()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        kalorimaaraeditTextNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                laskeKalorit()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tallennaButton.setOnClickListener {
            val nimi = ruokaEditText.text.toString()
            val maara = maaraSeekBar.progress
            val kalorit = kaloritextView.text.toString()
            val ruoka = Ruoka(nimi = nimi, maara = maara, kalorit = kalorit)
            //ruokaViewModel.insertRuoka(ruoka)
            println(ruoka.toString())
        }
        return view
    }
    private fun laskeKalorit() {
        val maara = maaraSeekBar.progress
        val arvo = kalorimaaraeditTextNumber.text.toString().toDoubleOrNull() ?: 0.0
        val tulos = maara * arvo / 100
        val tulosText = "$tulos kcal"
        kaloritextView.text = tulosText
    }
}