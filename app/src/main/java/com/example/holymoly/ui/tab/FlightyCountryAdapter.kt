package com.example.holymoly.ui.tab

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.holymoly.R

interface OnCountryItemSelectedListener {
    fun onCountryItemSelected(CountryItem : String)
}

class FlightyCountryAdapter(private val context: Context, private val spinner: Spinner, private val countries: List<String>) {
    private var listener : OnCountryItemSelectedListener ?= null
    init{
        val adapter = ArrayAdapter(context, R.layout.flighty_country_item, countries)
        spinner.adapter = adapter
        setYearItemListener()
    }

    fun setOnCountryItemSelectedListener(listener: FlightFragment){
        this.listener = listener
    }
    fun setYearItemListener(){
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCountry = countries[position]
                listener?.onCountryItemSelected(selectedCountry)
            }
        }
    }
}