package com.example.holymoly

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.holymoly.databinding.ActivityAddBinding
import com.example.holymoly.ui.FlightIconUpdateListener
import com.example.holymoly.ui.tab.CalendarFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Date

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    //firestore
    private val firestoreHelper = FirestoreHelper()
    private lateinit var broadcastManager: LocalBroadcastManager

    private var flightIconUpdateListener: FlightIconUpdateListener? = null

    // MainActivity에서 호출될 FlightIconUpdateListener 설정하는 메서드
    fun setFlightIconUpdateListener(listener: FlightIconUpdateListener) {
        flightIconUpdateListener = listener
    }

    // 예를 들어, Flight 버튼이 클릭되면 MainActivity에 있는 onUpdateFlightIcon 호출
    private fun onFlightButtonClick() {
        // Flight 버튼 클릭되면 이미지를 변경하고 MainActivity로 변경된 이미지 전달
        val newFlightIcon = R.drawable.ic_book // 새로운 아이콘 이미지
        flightIconUpdateListener?.onUpdateFlightIcon(newFlightIcon)
    }

    // 알림 시간 설정
    val checkboxList = listOf<String>("3일 전", "1일 전", "1시간 전", "30분 전")
    var selectedOption: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();


        binding.alarm.setOnClickListener{   // 알림 설정
            showCheckboxDialog()
        }

        broadcastManager = LocalBroadcastManager.getInstance(this)

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

            onFlightButtonClick()
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

            finish()

            if(flag == 1) {
                Toast.makeText(this, "항공권을 예약하러 가보세요!", Toast.LENGTH_LONG).show()
            }

        }
    }

    fun  showCheckboxDialog() { // 체크박스 보여지기
        val options = checkboxList.toTypedArray()
        val checkedItems = BooleanArray(checkboxList.size) { false }

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("원하는 알림 시간을 설정해주세요!")
        dialogBuilder.setMultiChoiceItems(options, checkedItems) { _, which, isChecked ->
            checkedItems[which] = isChecked
        }
        dialogBuilder.setPositiveButton("OK") { _, _ ->
            val selectedOptions = ArrayList<String>()
            for (i in checkedItems.indices) {
                if (checkedItems[i]) {
                    selectedOptions.add(checkboxList[i])
                }
            }
            selectedOption = selectedOptions.joinToString(", ")
            binding.alarm.text = selectedOption // alarm 텍스트뷰에 선택된 옵션 표시
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}