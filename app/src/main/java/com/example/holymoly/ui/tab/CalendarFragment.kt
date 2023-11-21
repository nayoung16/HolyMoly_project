package com.example.holymoly.ui.tab

import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentCalendarBinding
import java.util.Calendar
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding 초기화
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // 화면 설정
        setMonthView()

        binding.preBtn.setOnClickListener{
            CalendarUtil.selectedDate.add(Calendar.MONTH, -1) // 현재 달 -1
            setMonthView()
        }

        binding.nextBtn.setOnClickListener{
            CalendarUtil.selectedDate.add(Calendar.MONTH, 1) // 현재 달 +1
            setMonthView()
        }

        // 날짜 화면에 보여주기

        // Inflate the layout for this fragment
        return binding.root
    }

    fun setMonthView() {
        // 년월 텍스트 뷰 셋팅
        binding.monthYearText.text = monthYearFromDate(CalendarUtil.selectedDate)
        // 날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray()
        // 어댑터 초기화
        val adapter = CalendarAdapter(dayList)
        // 열 7개 생성
        var manager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 7)
        // 레이아웃 적용
        binding. recyclerView.layoutManager = manager
        // 어뎁터 적용
        binding.recyclerView.adapter = adapter
    }
    // 날짜 타입 설정(월, 년) -> 상단바
    fun monthYearFromDate(calendar: Calendar): String{
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH) + 1
        return "$month 월 $year"
    }
    // 날짜 생성
    fun dayInMonthArray(): ArrayList<Date>{
        var dayList = ArrayList<Date>()
        var monthCalendar = CalendarUtil.selectedDate.clone() as Calendar
        // 1일로 셋팅
        monthCalendar[Calendar.DAY_OF_MONTH] = 1
        // 해당 달의 1일의 요일
        val firstDayOfMonth = monthCalendar[Calendar.DAY_OF_WEEK] - 1
        // 요일 숫자만큼 이전 날짜로 설정
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)

        while(dayList.size < 42){
            dayList.add(monthCalendar.time)

            // 1일씩 늘린다
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dayList
    }



}