package com.example.holymoly.ui.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.holymoly.databinding.FragmentFlightBinding

class FlightFragment : Fragment(), OnCountryItemSelectedListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentFlightBinding.inflate(inflater, container, false)

        //나라 선택 어댑터
        val countryList = listOf("ICN", "TYO")
        //출발 나라
        val countryAdapterFrom = FlightyCountryAdapter(requireContext(), binding.flightyFrom, countryList)
        countryAdapterFrom.setOnCountryItemSelectedListener(this)
        //도착 나라
        val countryAdapterTo = FlightyCountryAdapter(requireContext(), binding.flightyTo, countryList)
        countryAdapterTo.setOnCountryItemSelectedListener(this)

        return binding.root
    }

    override fun onCountryItemSelected(CountryItem: String) {
    }
}