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
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment() , OnYearItemSelectedListener{
    private lateinit var binding : FragmentHomeBinding

    private val currentYear = LocalDate.now().year
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //연도 combo box 초기화
        val yearList = setYearList()
        val yearAdapter = SolyearAdapter(requireContext(), binding.solyearSpinner, yearList)
        yearAdapter.setOnYearItemSelectedListener(this)

        return binding.root
    }

    //년도 list
    private fun setYearList(): List<String>{
        var yearList = mutableListOf<String>()
        for(i in 0..2){
            yearList.add((currentYear+i).toString())}

        return yearList
    }

    //년도 콤보박스 선택시 정보 업데이트
    override fun onYearItemSelected(yearItem: String) {
        setMainPage(yearItem)
    }

    //main page에 공휴일 정보 표시하기
    private fun setMainPage(year: String){
        //공휴일 정보 가져오기
        val holy = HolyDay(year)
        val holidayDatas : List<List<String>>

        if(year == currentYear.toString()) { //올해일 경우
            //올해 남은 공휴일 수
            binding.restOfYear.text = holy.restHolyOfYear().toString()
            //이 달의 공휴일
            binding.holydaysOfMonthText.text = "이 달의 공휴일"
            holidayDatas = holy.HolyListOfMonth() }
        else{  //다른 년도일 경우
            binding.holydaysOfMonthText.text = year + "년도 첫 공휴일"
            binding.restOfYear.text = holy.totalHolyOfYear().toString() + "일"
            holidayDatas = holy.FirstHolyListOfMonth()}
        var datas: List<List<String>>

        if(holidayDatas.isEmpty()) {
            datas = listOf(
                listOf("Item1")
            )
            binding.holydaysOfMonthLayout.adapter = HolidayOfMonthAdapter(datas)
            binding.holydaysOfMonthLayout.layoutManager = LinearLayoutManager(requireContext())
        } else {
            //이달의 공휴일 리사이클러 뷰
            binding.holydaysOfMonthLayout.adapter = HolidayOfMonthAdapter(holidayDatas)
            binding.holydaysOfMonthLayout.layoutManager = LinearLayoutManager(requireContext())
        }


        //각 달의 공휴일 수
        val datas_each_month = mutableListOf<String>("Jan","Feb","March","April","May","June","July",
            "Aug","Sep","Oct","Nov","Dec")
        var datas_each_month_holidays = holy.restHolyOfMonth()

        binding.holidaysOfEachMonthLayout.adapter = HolidayEachMonthAdapter(datas_each_month, datas_each_month_holidays)
        binding.holidaysOfEachMonthLayout.layoutManager = GridLayoutManager(activity,2)
    }

}