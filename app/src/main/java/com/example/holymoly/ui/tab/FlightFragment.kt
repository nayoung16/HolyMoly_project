package com.example.holymoly.ui.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.holymoly.databinding.FragmentFlightBinding

class FlightFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentFlightBinding.inflate(inflater, container, false)

        return binding.root
    }
}