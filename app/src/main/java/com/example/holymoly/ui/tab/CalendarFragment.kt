package com.example.holymoly.ui.tab

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.AddActivity
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.HolyDay
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.time.LocalDate
import java.util.Calendar

class CalendarFragment : Fragment(){
    lateinit var binding: FragmentCalendarBinding
    //firestore
    private val firestoreHelper = FirestoreHelper()

    private var c_month = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding 초기화
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        return binding.root
    }

    var holyList1 = HolyDay("2023").holyDatesYear()
    var holyList2 = HolyDay("2024").holyDatesYear()
    var holyList3 = HolyDay("2025").holyDatesYear()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarview.apply {
            // 요일 지정
            setWeekDayLabels(arrayOf("MON", "TUE", "WEN", "THU", "FRI", "SAT", "SUN"))
            // 달력 상단에 '월 년'
            setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
            setHeaderTextAppearance(R.style.CalendarTitle)
            setPadding(0, -20, 0, 30)
            isDynamicHeightEnabled = true   // 달력이 유동적으로 길어졌다가 짧아졌다가 함
        }

        binding.calendarview.setSelectedDate(CalendarDay.today())   // 오늘 날짜에 동그라미


        binding.calendarview.addDecorators( // 데코레이터 추가
            TodayDecorator(), SatDecorator(), SunDecorator(), OtherMonth(CalendarDay.today().month), CustomDayDecorator(holyList1), CustomDayDecorator(holyList2), CustomDayDecorator(holyList3)
        )

        var c_year = CalendarDay.today().year   // 캘린더 화면으로 넘어왔을 때의 년도
        c_month = CalendarDay.today().month // 캘린더 화면으로 넘어왔을 때의 월
        var c_day = CalendarDay.today().day // 캘린더 화면으로 넘어왔을 때의 일


        binding.clickdate.text = "${c_year}년 ${c_month}월 ${c_day}일"     // 캘린더 화면으로 넘어왔을 때의 날짜를 띄워줌

        calldatabase(c_month)   // 정보 불러와서 도트 띄우기

        binding.calendarview.setOnDateChangedListener { widget, date, selected ->
            calldatabase(c_month)
            dateselection(date)
        }

        binding.calendarview.setOnMonthChangedListener { widget, date ->  // 달이 변경
            c_year = date.year // 현재 달력 연도
            c_month = date.month // 현재 달력 월
            calldatabase(c_month)   // 일정 불러와서 도트 띄우기
        }

    }

    //초기화
    fun initialize(c_month : Int){
        binding.calendarview.removeDecorators()
        binding.calendarview.invalidateDecorators()

        binding.calendarview.addDecorators(     // 달력 넘어갔을 때 데코레이터 다시 추가
            TodayDecorator(),
            SatDecorator(),
            SunDecorator(),
            OtherMonth(c_month),
            CustomDayDecorator(holyList1),
            CustomDayDecorator(holyList2),
            CustomDayDecorator(holyList3)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calldatabase(c_month: Int) {        // 도트 띄우기
        //데이터 읽어오고 ui 설정하는 함수
        firestoreHelper.getMonthHolidaysFromFirestore(c_month) { holidayList ->
            initialize(c_month)
            var dates4 = mutableListOf<CalendarDay>()

            holidayList.forEach { holiday ->
                val year4 = holiday["start_year"].toString().toInt()
                val month4 = holiday["start_month"].toString().toInt()
                val day4 = holiday["start_date"].toString().toInt()

                val year5 = holiday["end_year"].toString().toInt()
                val month5 = holiday["end_month"].toString().toInt()
                val day5 = holiday["end_date"].toString().toInt()

                val startCalendarDay = CalendarDay.from(year4, month4, day4)
                val endCalendarDay = CalendarDay.from(year5, month5, day5)

                val daysInRange = getDatesInRange(startCalendarDay, endCalendarDay)
                dates4.addAll(daysInRange)
                val decorator = EventDecorator(HashSet(dates4), requireContext())
                binding.calendarview.addDecorator(decorator)
            }
        }
    }

    fun dateselection(date: CalendarDay) {  // 리사이클러뷰에 정보 띄우기
        val year = date.year
        val month = date.month
        val day = date.day

        binding.clickdate.text = "${year}년 ${month}월 ${day}일"   // 클릭된 날짜 띄워주기

        val year1 = date.year.toString()
        val month1 = if (date.month < 10) "0${date.month}" else date.month.toString()
        val day1 = if (date.day < 10) "0${date.day}" else date.day.toString()

        val combinedDate = "$year1$month1$day1" // 내가 클릭한 날 스트링으로 저장

        firestoreHelper.getMonthHolidaysFromFirestore(month){ holidayList ->
            var datas_holidays_title = mutableListOf<String>()
            var datas_holidays_start_year = mutableListOf<String>()
            var datas_holidays_start_month= mutableListOf<String>()
            var datas_holidays_start_date= mutableListOf<String>()
            var datas_holidays_end_year = mutableListOf<String>()
            var datas_holidays_end_month= mutableListOf<String>()
            var datas_holidays_end_date= mutableListOf<String>()
            var datas_categories= mutableListOf<String>()
            if(holidayList.isNotEmpty()) {
                holidayList.forEach { holiday ->
                    val startYear = holiday["start_year"].toString()
                    val startMonth = holiday["start_month"] as Long
                    val startMonth1 =
                        if (startMonth.toInt() < 10) "0${startMonth.toInt()}" else (startMonth.toInt()).toString()
                    val startDate = holiday["start_date"] as Long
                    val startDate1 =
                        if (startDate.toInt() < 10) "0${startDate.toInt()}" else (startDate.toInt()).toString()
                    val start = "$startYear$startMonth1$startDate1" // 일정 시작 날짜

                    val endYear = holiday["end_year"].toString()
                    val endMonth = holiday["end_month"] as Long
                    val endMonth1 =
                        if (endMonth.toInt() < 10) "0${endMonth.toInt()}" else (endMonth.toInt()).toString()
                    val endDate = holiday["end_date"] as Long
                    val endDate1 =
                        if (endDate.toInt() < 10) "0${endDate.toInt()}" else (endDate.toInt()).toString()
                    val end = "$endYear$endMonth1$endDate1" // 일정 끝 날짜

                    // 클릭된 날짜가 해당 일정의 시작과 종료 날짜 사이에 있는지 확인
                    if (combinedDate.toInt() in start.toInt()..end.toInt()) {
                        datas_holidays_title.add(holiday["holiday_title"].toString())
                        datas_holidays_start_year.add(holiday["start_year"].toString())
                        datas_holidays_start_month.add(holiday["start_month"].toString())
                        datas_holidays_start_date.add(holiday["start_date"].toString())
                        datas_holidays_end_year.add(holiday["end_year"].toString())
                        datas_holidays_end_month.add(holiday["end_month"].toString())
                        datas_holidays_end_date.add(holiday["end_date"].toString())
                        datas_categories.add(holiday["category"].toString())
                    }
                }
            }
            binding.scheduleRecycler.post {
                binding.scheduleRecycler.adapter = UpcomingSchedulesAdapter(
                    datas_holidays_title,
                    datas_holidays_start_year,
                    datas_holidays_start_month,
                    datas_holidays_start_date,
                    datas_holidays_end_year,
                    datas_holidays_end_month,
                    datas_holidays_end_date,
                    datas_categories)
                binding.scheduleRecycler.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        binding.fab.setOnClickListener{ // +버튼 누르면 일정 추가!
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("selectedYear", year)
            intent.putExtra("selectedMonth", month)
            intent.putExtra("selectedDay", day)
            startActivity(intent)
        }
    }

    class CustomDayDecorator(private val datesToDecorate: List<String>) : DayViewDecorator {    // 공휴일
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            val formattedDateString = "${day?.year}${String.format("%02d", day?.month)}${String.format("%02d", day?.day)}"
            Log.d("j", "${datesToDecorate.toString()}")
            return datesToDecorate.contains(formattedDateString)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    class EventDecorator(       // dot찍는 데코레이터
        private val dates: HashSet<CalendarDay>,
        private val context: Context
    ) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            val color = ContextCompat.getColor(context, R.color.purple_dot)
            view?.addSpan(DotSpan(8f, color))
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDatesInRange(startDate: CalendarDay, endDate: CalendarDay): List<CalendarDay> {  // 사이의 날들 계산
        val datesInRange = mutableListOf<CalendarDay>()

        var currentDate = LocalDate.of(startDate.year, startDate.month, startDate.day)

        while (!currentDate.isAfter(LocalDate.of(endDate.year, endDate.month, endDate.day))) {
            datesInRange.add(CalendarDay.from(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth))
            currentDate = currentDate.plusDays(1)
        }

        return datesInRange
    }


    class TodayDecorator : DayViewDecorator {
        val date = CalendarDay.today()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day == date
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(StyleSpan(Typeface.BOLD))
            view?.addSpan(RelativeSizeSpan(1.3f))
        }
    }

    class SatDecorator : DayViewDecorator { // 토요일 파란색
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.day?.let { dayOfMonth ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, day.year)
                    set(Calendar.MONTH, day.month - 1) // Calendar.MONTH는 0부터 시작하므로 1을 빼기
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
                weekDay == Calendar.SATURDAY
            } ?: false
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.BLUE))
        }
    }

    class SunDecorator : DayViewDecorator { // 일요일 빨간색
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.day?.let { dayOfMonth ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, day.year)
                    set(Calendar.MONTH, day.month - 1) // Calendar.MONTH는 0부터 시작하므로 1을 빼줍니다.
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
                weekDay == Calendar.SUNDAY
            } ?: false
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    class OtherMonth(val selectedMonth: Int) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.month != selectedMonth
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.GRAY))
        }
    }

}