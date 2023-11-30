package com.example.holymoly.ui.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.R
import com.example.holymoly.databinding.MainEachMonthHolidaysviewBinding

class MyViewHolder2(val binding: MainEachMonthHolidaysviewBinding)
    : RecyclerView.ViewHolder(binding.root)

class HolidayEachMonthAdapter(val datas_each_month : List<String>, val datas_each_month_holidays : List<Int>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int = datas_each_month.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder2).binding
        binding.eachMonth.text = datas_each_month[position]
        binding.eachMonthHolidays.text = datas_each_month_holidays[position+1].toString()

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