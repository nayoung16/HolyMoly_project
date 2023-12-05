package com.example.holymoly.ui.tab

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.holymoly.databinding.FragmentFirstBinding

class FirstFragment: Fragment() {
    lateinit var binding: FragmentFirstBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addBucket.setOnClickListener{
            val intent = Intent(context, AddBucket::class.java)
            startActivity(intent)
        }
    }
}
