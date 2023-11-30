package com.example.holymoly.ui.tab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.R

data class ScheduleItem(val title: String, val date: String)

class UpcomingSchedulesAdapter(val itemList: ArrayList<ScheduleItem>) :
    RecyclerView.Adapter<UpcomingSchedulesAdapter.ViewHolder>() {
    var titles = arrayOf("one", "two", "three")
    var details = arrayOf("Item one", "Item two", "Item three")
    var images = intArrayOf(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.title_recycler.text = itemList[position].title
        //holder.date_recycler.text = itemList[position].date
        holder.title_recycler.setText(titles.get(position))
        holder.item_image.setImageResource(images.get(position))
        holder.date_recycler.setText(details.get(position))
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title_recycler = itemView.findViewById<TextView>(R.id.title_recycler)
        val date_recycler = itemView.findViewById<TextView>(R.id.date_recycler)
        var item_image = itemView.findViewById<ImageView>(R.id.item_image)
    }
}
