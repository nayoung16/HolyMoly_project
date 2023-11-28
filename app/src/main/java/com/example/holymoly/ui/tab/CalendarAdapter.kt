package com.example.holymoly.ui.tab

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.AddActivity
import com.example.holymoly.R
import com.google.android.material.internal.ContextUtils.getActivity
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class CalendarAdapter(val dayList: ArrayList<Date>):
    RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>(){
        class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val dayText: TextView = itemView.findViewById(R.id.dayText)
        }

    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)
        return ItemViewHolder(view)
    }

    // 데이터 설정
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // 날짜 변수
        var monthDate = dayList[holder.adapterPosition]
        // 초기화
        var dateCalendar = Calendar.getInstance()
        // 날짜 캘린더에 담기
        dateCalendar.time = monthDate
        // 캘린더값 날짜 변수에 담기
        var dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH)

        holder.dayText.text = dayNo.toString()

        var iYear = dateCalendar.get(Calendar.YEAR) // 년
        var iMonth = dateCalendar.get(Calendar.MONTH) + 1   // 월
        var iDay = dateCalendar.get(Calendar.DAY_OF_MONTH)  // 일

        var selectYear = CalendarUtil.selectedDate.get(Calendar.YEAR)
        var selectMonth = CalendarUtil.selectedDate.get(Calendar.MONTH) + 1
        var selectDay = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH)

        if (iYear == selectYear && iMonth == selectMonth) {
            holder.dayText.setTextColor(Color.parseColor("#000000"))

            // 현재 날짜 비교해서 같다면 배경색상 변경
            if (dayNo == selectDay) {
                holder.itemView.setBackgroundColor(R.color.gblue)
            }
            // 토 일 색상 변경
            if ((position + 1) % 7 == 0) {
                holder.dayText.setTextColor(Color.BLUE)
            }
            else if (position == 0 || position % 7 == 0) {
                holder.dayText.setTextColor(Color.RED)
            }
        }
        else{
            holder.dayText.setTextColor(Color.GRAY)

            // 토 일 색상 변경
            if ((position + 1) % 7 == 0) {
                holder.dayText.setTextColor(Color.GRAY)
            }
            else if (position == 0 || position % 7 == 0) {
                holder.dayText.setTextColor(Color.GRAY)
            }
        }

        // 날짜 클릭 이벤트
        holder.itemView.setOnClickListener{
            // 인터페이스로 날짜 넘겨주기
            val intent = Intent(holder.itemView?.context, AddActivity::class.java)
            ContextCompat.startActivity(holder.itemView.context, intent, null)


//            var yearMonDay = "$iYear 년 $iMonth 월 $iDay 일"
//            Toast.makeText(holder.itemView.context, yearMonDay, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }
}