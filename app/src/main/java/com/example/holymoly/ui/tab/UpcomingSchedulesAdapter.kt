package com.example.holymoly.ui.tab

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.R
import com.example.holymoly.databinding.ItemScheduleBinding



class MyViewHolder3(val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

//firestore
private val firestoreHelper = FirestoreHelper()
class UpcomingSchedulesAdapter(
    val datas_holidays_title: MutableList<String>, val datas_holidays_start_year: MutableList<String>,
    val datas_holidays_start_month: MutableList<String>,val datas_holidays_start_date: MutableList<String>,
    val datas_holidays_end_year: MutableList<String>, val datas_holidays_end_month: MutableList<String>,
    val datas_holidays_end_date: MutableList<String>, val datas_categories: MutableList<String>
) :
    RecyclerView.Adapter<MyViewHolder3>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder3 {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemScheduleBinding.inflate(inflater, parent, false)
        return MyViewHolder3(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder3, position: Int) {
        when (datas_categories[position]) {     // 카테고리에 맞춰서 이미지 띄우기
            "0" -> holder.binding.itemImage.setImageResource(R.drawable.ic_people)
            "1" -> holder.binding.itemImage.setImageResource(R.drawable.ic_airplane_takeoff)
            "2" -> holder.binding.itemImage.setImageResource(R.drawable.ic_book)
            "3" -> holder.binding.itemImage.setImageResource(R.drawable.ic_movie)
        }

        holder.binding.titleRecycler.text = datas_holidays_title[position]  // 일정 제목

        if ( datas_holidays_start_year[position] == datas_holidays_end_year[position]
            && datas_holidays_start_month[position] == datas_holidays_end_month[position]
            && datas_holidays_start_date[position] == datas_holidays_end_date[position]) {  // 일정 시작, 끝
            holder.binding.dateRecycler.text = datas_holidays_start_month[position] + "월" + datas_holidays_start_date[position] + "일"
        }
        else {
            holder.binding.dateRecycler.text = datas_holidays_start_month[position] + "월" + datas_holidays_start_date[position] + "일" + " ~ " + datas_holidays_end_month[position] + "월" + datas_holidays_end_date[position] + "일"
        }

        holder.binding.btnDelete.setOnClickListener{    // 일정 삭제 버튼 누르면
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setMessage("해당 일정을 삭제하시겠습니까?")
            alertDialogBuilder.setPositiveButton("삭제") { dialog, which ->

                val delete_title = datas_holidays_title[position]

                // 여기서 삭제
                firestoreHelper.deleteHolidaysFromFirestore(delete_title)
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
        return datas_holidays_title.size
    }

}
