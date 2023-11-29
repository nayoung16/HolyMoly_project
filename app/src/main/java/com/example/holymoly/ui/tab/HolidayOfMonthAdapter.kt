package com.example.holymoly.ui.tab

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
        binding.holidaysOfMonthName.text = datas[position][0]
        binding.holidaysOfMonthDate.visibility = View.VISIBLE
        binding.holidaysOfMonthDate.text = datas[position][1] + " ~ " + datas[position][2]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = MainMonthHolidaysviewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

}