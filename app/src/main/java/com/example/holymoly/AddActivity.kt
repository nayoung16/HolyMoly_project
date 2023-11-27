package com.example.holymoly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.holymoly.databinding.ActivityAddBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Date

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();

        val sharedPreference = getSharedPreferences("AddActivity", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        binding.choosedate.setOnClickListener() {
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())

            val builder = MaterialDatePicker.Builder.datePicker().setTitleText("Set date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(calendarConstraintBuilder.build());

            val datePicker = builder.build()

            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar.time = Date(it)
                val calendarMilli = calendar.timeInMillis

                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val year = calendar.get(Calendar.YEAR)
                binding.choosedate.text = "${year}년 ${month + 1}월 ${day}일"

                editor.putLong("Die_Millis", calendarMilli)
                editor.apply()
            }
            datePicker.show(supportFragmentManager, datePicker.toString())
        }
    }
}
