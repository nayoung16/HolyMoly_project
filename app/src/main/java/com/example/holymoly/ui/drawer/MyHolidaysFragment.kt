package com.example.holymoly.ui.drawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentMyHolidaysBinding
import com.example.holymoly.databinding.ItemMyHolidaysBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyViewHolder(val binding: ItemMyHolidaysBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas_holidays_title: MutableList<String>, val datas_holidays_start_date: MutableList<String>,
    val datas_holidays_end_date: MutableList<String>, val datas_categories: MutableList<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas_holidays_title.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemMyHolidaysBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemImageHolidays.setImageResource(R.drawable.ic_movie)
        binding.itemDataHolidays.text=datas_holidays_title[position]
        binding.itemDataHolidaysStartDate.text = datas_holidays_start_date[position]
        binding.itemDataHolidaysEndDate.text = datas_holidays_end_date[position]
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

        // FirebaseAuth 인스턴스 가져오기
        val auth = Firebase.auth

        // 현재 로그인된 사용자 가져오기
        val currentUser = auth.currentUser

        // 현재 로그인된 사용자의 이메일 가져오기
        val userEmail = currentUser?.email



        val datas_holidays_title = mutableListOf<String>("세부 여행","일정2","일정3")
        val datas_holidays_start_date = mutableListOf<String>("12월 24일","12월 24일","12월 24일")
        val datas_holidays_end_date = mutableListOf<String>("12월 24일","12월 24일","12월 24일")
        val datas_categories = mutableListOf<String>("movie","book","flght")


        val layoutManager = LinearLayoutManager(activity)
        binding.myHolidaysRecyclerView.layoutManager = layoutManager
        val adapter = MyAdapter(datas_holidays_title, datas_holidays_start_date,
            datas_holidays_end_date,datas_categories)
        binding.myHolidaysRecyclerView.adapter = adapter
        binding.myHolidaysRecyclerView.addItemDecoration(MyDecoration(activity as Context))
        return binding.root
    }

}