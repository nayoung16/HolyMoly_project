package com.example.holymoly.ui.drawer

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentMyHolidaysBinding
import com.example.holymoly.databinding.ItemMyHolidaysBinding
import kotlinx.coroutines.launch

class MyViewHolder(val binding: ItemMyHolidaysBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas_holidays_title: MutableList<String>, val datas_holidays_start_year: MutableList<String>,
                val datas_holidays_start_month: MutableList<String>,val datas_holidays_start_date: MutableList<String>,
                val datas_holidays_end_year: MutableList<String>, val datas_holidays_end_month: MutableList<String>,
                val datas_holidays_end_date: MutableList<String>, val datas_categories: MutableList<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas_holidays_title.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemMyHolidaysBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        when (datas_categories[position]) {
            "0" -> binding.itemImageHolidays.setImageResource(R.drawable.ic_people)
            "1" -> binding.itemImageHolidays.setImageResource(R.drawable.ic_airplane_takeoff)
            "2" -> binding.itemImageHolidays.setImageResource(R.drawable.ic_book)
            "3" -> binding.itemImageHolidays.setImageResource(R.drawable.ic_movie)
        }

        binding.itemDataHolidays.text=datas_holidays_title[position]
        if ( datas_holidays_start_year[position] == datas_holidays_end_year[position]
            && datas_holidays_start_month[position] == datas_holidays_end_month[position]
            && datas_holidays_start_date[position] == datas_holidays_end_date[position]) {
            binding.itemDataHolidaysStartDate.text = datas_holidays_start_month[position] + "월" + datas_holidays_start_date[position] + "일"
            binding.itemDataHolidaysEndDate.text = " "
        }
        else {
            binding.itemDataHolidaysStartDate.text = datas_holidays_start_month[position] + "월" + datas_holidays_start_date[position] + "일"
            binding.itemDataHolidaysEndDate.text = " ~ " + datas_holidays_end_month[position] + "월" + datas_holidays_end_date[position] + "일"
        }

    }
}

class MyDecoration(val context: Context): RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(10,10,10,0)
        ViewCompat.setElevation(view, 20.0f)
    }
}
class MyHolidaysFragment : Fragment() {
    //firestore
    private val firestoreHelper = FirestoreHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyHolidaysBinding.inflate(inflater, container, false)



        lifecycleScope.launch {

            try {
                val holidayList = firestoreHelper.getAllHolidaysFromFirestore()

                // holidayList를 사용하여 UI에 데이터를 적용하는 작업 수행
                // 예를 들어, RecyclerView의 어댑터에 데이터를 설정하거나 화면에 출력
                Log.d("ny", "Received holidayList: $holidayList")

                val datas_holidays_title = mutableListOf<String>()
                val datas_holidays_start_year = mutableListOf<String>()
                val datas_holidays_start_month = mutableListOf<String>()
                val datas_holidays_start_date = mutableListOf<String>()
                val datas_holidays_end_year = mutableListOf<String>()
                val datas_holidays_end_month = mutableListOf<String>()
                val datas_holidays_end_date = mutableListOf<String>()
                val datas_categories = mutableListOf<String>()

                for (holiday in holidayList) {
                    datas_holidays_title.add(holiday["holiday_title"].toString())
                    datas_holidays_start_year.add(holiday["start_year"].toString())
                    datas_holidays_start_month.add(holiday["start_month"].toString())
                    datas_holidays_start_date.add(holiday["start_date"].toString())
                    datas_holidays_end_year.add(holiday["end_year"].toString())
                    datas_holidays_end_month.add(holiday["end_month"].toString())
                    datas_holidays_end_date.add(holiday["end_date"].toString())
                    datas_categories.add(holiday["category"].toString())
                }

                val adapter = MyAdapter(datas_holidays_title, datas_holidays_start_year, datas_holidays_start_month,
                    datas_holidays_start_date, datas_holidays_end_year, datas_holidays_end_month,
                    datas_holidays_end_date ,datas_categories)

                val layoutManager = LinearLayoutManager(activity)
                binding.myHolidaysRecyclerView.layoutManager = layoutManager

                binding.myHolidaysRecyclerView.adapter = adapter
                binding.myHolidaysRecyclerView.addItemDecoration(MyDecoration(activity as Context))


            } catch (e: Exception) {
                // 예외 처리
                Log.e(TAG, "Error fetching holidays: $e")
            }

        }





        return binding.root
    }

}