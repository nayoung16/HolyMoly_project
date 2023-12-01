package com.example.holymoly.ui.tab

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.R
import com.example.holymoly.databinding.MainEachMonthHolidaysviewBinding
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
private val currentMonth: Int = LocalDate.now().month.value
@RequiresApi(Build.VERSION_CODES.O)
private val currentYear: Int = LocalDate.now().year

class MyViewHolder2(val binding: MainEachMonthHolidaysviewBinding)
    : RecyclerView.ViewHolder(binding.root)

class HolidayEachMonthAdapter(
    val datas_each_month: List<String>, val datas_each_month_holidays: List<Int>
    , val year: String
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int = datas_each_month.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder2).binding
        binding.eachMonth.text = datas_each_month[position]
        val current_month_position = currentMonth - 1
        val selected_year = year
        // 여기에 조건을 추가하여 특정 아이템의 배경을 변경

        if (selected_year == currentYear.toString()) {
            if (position < current_month_position) {
                // 배경을 다르게 설정하는 코드
                binding.root.setBackgroundResource(R.drawable.main_each_month_holidays_box)
                binding.eachMonthHolidays.text = ""
            } else {
                // 다른 아이템들의 배경 설정
                binding.root.setBackgroundResource(R.drawable.main_current_month_holidays_box)
                binding.eachMonthHolidays.text = datas_each_month_holidays[position+1].toString()
            }
        } else {
            binding.root.setBackgroundResource(R.drawable.main_current_month_holidays_box)
            binding.eachMonthHolidays.text = datas_each_month_holidays[position+1].toString()
        }



        holder.itemView.setOnClickListener{

            // 여기에 해당 월로 이동하는 로직 추가
            val selectedMonthIndex = position // 클릭한 월의 인덱스

            // Fragment를 이동할 때 Bundle을 사용하여 데이터를 전달
            val bundle = Bundle()
            bundle.putInt("selectedMonthIndex", selectedMonthIndex)

            val fragment = CalendarFragment()
            fragment.arguments = bundle

            val transaction = (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.calendarpage, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MainEachMonthHolidaysviewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder2(binding)
    }
}