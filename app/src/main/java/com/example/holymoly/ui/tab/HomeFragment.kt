package com.example.holymoly.ui.tab

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.HolyDay
import com.example.holymoly.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [BucketListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val holy = HolyDay("2023")
        //올해의 남은 공휴일 수
        binding.restOfYear.text = holy.restHolyOfYear().toString()

        //이 달의 공휴일
        val datas : List<List<String>> = holy.HolyListOfMonth()
        binding.holydaysOfMonthLayout.adapter = HolidayOfMonthAdapter(datas)
        binding.holydaysOfMonthLayout.layoutManager = LinearLayoutManager(activity)

        //각 달의 공휴일 수
        val datas_each_month = mutableListOf<String>("Jan","Feb","March","April","May","June","July",
            "Aug","Sep","Oct","Nov","Dec")
        val datas_each_month_holidays = mutableListOf<String>("1","2","3","4","5","6","7","8","9","10","11","12")
        binding.holidaysOfEachMonthLayout.adapter = HolidayEachMonthAdapter(datas_each_month, datas_each_month_holidays)
        binding.holidaysOfEachMonthLayout.layoutManager = GridLayoutManager(activity,2)

        return binding.root
    }

}