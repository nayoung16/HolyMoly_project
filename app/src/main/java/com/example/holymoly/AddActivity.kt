package com.example.holymoly

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.holymoly.databinding.ActivityAddBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Date

class AddActivity : AppCompatActivity(){
    lateinit var binding: ActivityAddBinding
    //firestore
    private val firestoreHelper = FirestoreHelper()
    private val viewModel: DBViewModel by viewModels()
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();

        val sharedPreference = getSharedPreferences("AddActivity", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()

        // calendarfragment에서 넘어온 값
        val selectedYear = intent.getIntExtra("selectedYear", 0)
        val selectedMonth = intent.getIntExtra("selectedMonth", 0)
        val selectedDay = intent.getIntExtra("selectedDay", 0)

        binding.todaydate.text = "${selectedYear}년\n${selectedMonth}월 ${selectedDay}일"

        binding.choosedate.text = "${selectedYear}년\n${selectedMonth}월 ${selectedDay}일"

        var month = 0
        var day = 0
        var year = 0

        binding.choosedate.setOnClickListener() {   // 일정이 끝나는 날 설정하기
            val calendarConstraintBuilder = CalendarConstraints.Builder()

            val builder = MaterialDatePicker.Builder.datePicker().setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(calendarConstraintBuilder.build());

            if (selectedYear != 0 && selectedMonth != 0 && selectedDay != 0) {  // 기존에 받아온 날짜 데이트피커로 넘기기
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth - 1, selectedDay)
                builder.setSelection(selectedCalendar.timeInMillis)
            }

            val datePicker = builder.build()

            datePicker.addOnPositiveButtonClickListener {   // 데이트피커에서 날짜를 고르고 ok누르면
                val calendar = Calendar.getInstance()
                calendar.time = Date(it)
                val calendarMilli = calendar.timeInMillis

                month = calendar.get(Calendar.MONTH)
                day = calendar.get(Calendar.DAY_OF_MONTH)
                year = calendar.get(Calendar.YEAR)
                binding.choosedate.text = "${year}년\n${month + 1}월 ${day}일"

                editor.putLong("Die_Millis", calendarMilli)
                editor.apply()
            }
            datePicker.show(supportFragmentManager, datePicker.toString())
        }

        var flag = 0
        var isBase = true // 초기상태
        binding.flight.setOnClickListener(){    // 비행기
            flag = if(isBase) {
                1
            } else {
                0
            }
            val color = if(isBase) {
                ContextCompat.getColor(this, R.color.purple_200)
            } else {
                ContextCompat.getColor(this, R.color.btn_base)
            }
            binding.flight.setBackgroundColor(color)
            isBase = !isBase
        }

        binding.movie.setOnClickListener(){ // 영화
            flag = if(isBase) {
                2
            } else {
                0
            }
            val color = if(isBase) {
                ContextCompat.getColor(this, R.color.purple_200)
            } else {
                ContextCompat.getColor(this, R.color.btn_base)
            }
            binding.movie.setBackgroundColor(color)
            isBase = !isBase
        }

        binding.book.setOnClickListener(){  // 책
            flag = if(isBase) {
                3
            } else {
                0
            }
            val color = if(isBase) {
                ContextCompat.getColor(this, R.color.purple_200)
            } else {
                ContextCompat.getColor(this, R.color.btn_base)
            }
            binding.book.setBackgroundColor(color)
            isBase = !isBase
        }

        binding.cancel.setOnClickListener(){    // 취소 시 이전 화면으로
            finish()
        }
        binding.save.setOnClickListener(){  // 저장 클릭 시 캘린더에 표시
            // 일정 제목
            val title = binding.title1.text.toString()
            // 시작 날
            val s_year = intent.getIntExtra("selectedYear", 0)
            val s_month = intent.getIntExtra("selectedMonth", 0)
            val s_day = intent.getIntExtra("selectedDay", 0)
            // 끝나는 날
            val e_year = year
            val e_month = month + 1
            val e_day = day
            // 비행기, 영화, 책 중 택1
            var cate = flag

            firestoreHelper.addHolidayToFirestore(title, s_year, s_month, s_day, e_year, e_month, e_day, cate)

            // ViewModel에 값을 설정
            viewModel.updateFlagDB(1)
            finish()
        }
    }


}