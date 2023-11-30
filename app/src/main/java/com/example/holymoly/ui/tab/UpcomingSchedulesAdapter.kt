package com.example.holymoly.ui.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.databinding.ItemScheduleBinding

class MyViewHolder3(val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

class UpcomingSchedulesAdapter(private val titles: List<String>, private val details: List<String>, private val images: List<Int>) :
    RecyclerView.Adapter<MyViewHolder3>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder3 {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemScheduleBinding.inflate(inflater, parent, false)
        return MyViewHolder3(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder3, position: Int) {
        val currentTitle = titles[position]
        val currentDetail = details[position]
        val currentImage = images[position]

        holder.binding.titleRecycler.text = currentTitle
        holder.binding.dateRecycler.text = currentDetail
        holder.binding.itemImage.setImageResource(currentImage)
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}
