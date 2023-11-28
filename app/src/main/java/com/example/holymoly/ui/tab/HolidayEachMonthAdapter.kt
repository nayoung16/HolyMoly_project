package com.example.holymoly.ui.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.databinding.MainEachMonthHolidaysviewBinding

class MyViewHolder2(val binding: MainEachMonthHolidaysviewBinding)
    : RecyclerView.ViewHolder(binding.root)

class HolidayEachMonthAdapter(val datas_each_month : List<String>, val datas_each_month_holidays : List<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int = datas_each_month.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder2).binding
        binding.eachMonth.text = datas_each_month[position]
        binding.eachMonthHolidays.text = datas_each_month_holidays[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MainEachMonthHolidaysviewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder2(binding)
    }
}