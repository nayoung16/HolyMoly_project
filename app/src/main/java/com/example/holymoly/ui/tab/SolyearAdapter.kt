package com.example.holymoly.ui.tab

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.holymoly.R

interface OnYearItemSelectedListener {
    fun onYearItemSelected(yearItem : String)
}

class SolyearAdapter(private val context: Context, private val spinner: Spinner, private val years: List<String>) {
    private var listener : OnYearItemSelectedListener ?= null
    init{
        val adapter = ArrayAdapter(context, R.layout.main_solyear_item, years)
        spinner.adapter = adapter
        setYearItemListener()
    }

    fun setOnYearItemSelectedListener(listener: HomeFragment){
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
                val selectedYear = years[position]
                listener?.onYearItemSelected(selectedYear)
            }
        }
    }
}