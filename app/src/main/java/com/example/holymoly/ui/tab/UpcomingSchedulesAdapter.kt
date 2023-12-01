package com.example.holymoly.ui.tab

import android.app.AlertDialog
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

        holder.binding.btnDelete.setOnClickListener{
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setMessage("해당 일정을 삭제하시겠습니까?")
            alertDialogBuilder.setPositiveButton("삭제") { dialog, which ->
                // db에서 일정 삭제!

                // 삭제 후 다이얼로그 닫기
                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}
