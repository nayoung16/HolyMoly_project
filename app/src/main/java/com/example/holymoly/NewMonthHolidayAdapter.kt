package com.example.holymoly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.databinding.ItemMonthHolidaysBinding

class MonthHolidaysViewHolder(val binding: ItemMonthHolidaysBinding)
    : RecyclerView.ViewHolder(binding.root)

class NewMonthHolidayAdapter(val datas : List<List<String>>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMonthHolidaysBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MonthHolidaysViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MonthHolidaysViewHolder).binding

        binding.holidaysOfMonthName2.text = datas[position][0]
        binding.holidaysOfMonthDate2.visibility = View.VISIBLE

        var text_str : String = ""
        var date_str : String
        if (datas[position][1] == datas[position][2]) {
            date_str = datas[position][1]
            text_str += date_str.substring(4,6) + "월 "
            text_str += date_str.substring(6) + "일"
        }
        else {
            for (i in 1..2) {
                date_str = datas[position][i]
                text_str += date_str.substring(4, 6) + "월 "
                text_str += date_str.substring(6) + "일"
                if (i == 1)
                    text_str += " ~ "
            }
        }

        binding.holidaysOfMonthDate2.text = text_str
    }

}