package com.example.holymoly.ui.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentSecondBinding

class SecondFragment: Fragment() {
    lateinit var binding : FragmentSecondBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_second, container, false)
    }
}