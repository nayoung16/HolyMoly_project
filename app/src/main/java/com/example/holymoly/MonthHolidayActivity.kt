package com.example.holymoly

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.databinding.ActivityMonthHolidayBinding
import com.example.holymoly.ui.tab.HolidayOfMonthAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthHolidayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthHolidayBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMonthHolidayBinding.inflate(layoutInflater)

        binding.popupBackbtn.setOnClickListener { //창 닫
            finish()
        }
        setContentView(binding.root)

        val selectedYear = intent.getStringExtra("year")
        val selectedMonth = intent.getIntExtra("month",0)

        Log.d("ny", "${selectedYear}/${selectedMonth}")

        binding.aboutYear.text = selectedYear+"년"
        binding.aboutMonth.text = selectedMonth.toString()+"월"

        GlobalScope.launch(Dispatchers.Main){
            val holy = fetchHolyDay(selectedYear.toString())

            val holidayDatas : List<List<String>> = holy.AllHolyListOfMonth(selectedMonth)

            Log.d("ny",holidayDatas.toString())

            binding.aboutHoliday.adapter = HolidayOfMonthAdapter(holidayDatas)
            binding.aboutHoliday.layoutManager = LinearLayoutManager(this@MonthHolidayActivity)
        }

    }

    private suspend fun fetchHolyDay(year: String): HolyDay{
        //HolyDay의 값을 비동기적으로 가져옴
        return withContext(Dispatchers.IO){
            HolyDay(year)
        }
    }
}