package com.example.holymoly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.holymoly.databinding.ActivityAddBinding
import com.example.holymoly.ui.tab.CalendarAdapter
import com.example.holymoly.ui.tab.CalendarFragment
import com.example.holymoly.ui.tab.CalendarUtil
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

        val calendar = Calendar.getInstance()


        val selectedYear = intent.getIntExtra("selectedYear", 0)
        val selectedMonth = intent.getIntExtra("selectedMonth", 0)
        val selectedDay = intent.getIntExtra("selectedDay", 0)

        binding.todaydate.text = "${selectedYear}년\n${selectedMonth}월 ${selectedDay}일"

        binding.choosedate.text = "${selectedYear}년\n${selectedMonth}월 ${selectedDay}일"

        binding.choosedate.setOnClickListener() {
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())

            val builder = MaterialDatePicker.Builder.datePicker().setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(calendarConstraintBuilder.build());

            if (selectedYear != 0 && selectedMonth != 0 && selectedDay != 0) {  // 기존에 받아온 날짜 데이트피커로 넘기기
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                builder.setSelection(selectedCalendar.timeInMillis)
            }

            val datePicker = builder.build()

            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar.time = Date(it)
                val calendarMilli = calendar.timeInMillis

                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val year = calendar.get(Calendar.YEAR)
                binding.choosedate.text = "${year}년\n${month + 1}월 ${day}일"

                editor.putLong("Die_Millis", calendarMilli)
                editor.apply()
            }
            datePicker.show(supportFragmentManager, datePicker.toString())
        }
        binding.cancel.setOnClickListener(){    // 취소 시 이전 화면으로
            finish()
        }
        binding.save.setOnClickListener(){  // 저장 클릭 시 캘린더에 표시

        }
    }
}
