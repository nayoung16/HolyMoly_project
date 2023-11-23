package com.example.holymoly.ui.drawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentMyHolidaysBinding
import com.example.holymoly.databinding.ItemMyHolidaysBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [MyHolidaysFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MyViewHolder(val binding: ItemMyHolidaysBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<String>, val datas2: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemMyHolidaysBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemImageHolidays.setImageResource(R.drawable.ic_movie)
        binding.itemDataHolidays.text=datas[position]
        binding.itemDataHolidaysSchedule.text = datas2[position]
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyHolidaysBinding.inflate(inflater, container, false)

        val datas_holidays = mutableListOf<String>("세부 여행","일정2","일정3")
        val datas_holidays_schedule = mutableListOf<String>("12월 24일 - 12월 28일","12월 24일 - 12월 28일","12월 24일 - 12월 28일")

        val layoutManager = LinearLayoutManager(activity)
        binding.myHolidaysRecyclerView.layoutManager = layoutManager
        val adapter = MyAdapter(datas_holidays, datas_holidays_schedule)
        binding.myHolidaysRecyclerView.adapter = adapter
        binding.myHolidaysRecyclerView.addItemDecoration(MyDecoration(activity as Context))
        return binding.root
    }

}