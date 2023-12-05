package com.example.holymoly.ui.tab

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.databinding.MainMonthHolidaysviewBinding

class MyViewHolder(val binding: MainMonthHolidaysviewBinding)
    : RecyclerView.ViewHolder(binding.root)

/*대상 객체에 따라 바뀜 -> 제너릭*/
class HolidayOfMonthAdapter (val datas : List<List<String>>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){



    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding

        if(datas[0][0] == "Item1") {
            Log.d("ny","test")
            binding.holidaysOfMonthName.text = "이번 달은"
            binding.holidaysOfMonthName.textSize = 16f
            binding.holidaysOfMonthDate.visibility = View.VISIBLE
            binding.holidaysOfMonthDate.text = "공휴일이 없어요"
        }
        else {
            binding.holidaysOfMonthName.text = datas[position][0]
            binding.holidaysOfMonthDate.visibility = View.VISIBLE
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

            binding.holidaysOfMonthDate.text = text_str
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = MainMonthHolidaysviewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

}