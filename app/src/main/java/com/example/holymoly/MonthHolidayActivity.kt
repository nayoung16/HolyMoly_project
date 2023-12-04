package com.example.holymoly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.holymoly.databinding.ActivityMonthHolidayBinding

class MonthHolidayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthHolidayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMonthHolidayBinding.inflate(layoutInflater)

        binding.popupBackbtn.setOnClickListener { //창 닫
            finish()
        }
        setContentView(binding.root)
    }

}