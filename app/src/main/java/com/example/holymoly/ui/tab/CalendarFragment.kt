package com.example.holymoly.ui.tab

import android.content.ContentValues
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.AddActivity
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding
    //firestore
    private val firestoreHelper = FirestoreHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding 초기화
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

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
            isDynamicHeightEnabled = true
        }
        binding.calendarview.setSelectedDate(CalendarDay.today())

        binding.calendarview.addDecorators(
            TodayDecorator(), SatDecorator(), SunDecorator(), OtherMonth(CalendarDay.today().month)
        )


        var c_year = CalendarDay.today().year   // 캘린더 화면으로 넘어왔을 때의 년도
        var c_month = CalendarDay.today().month // 캘린더 화면으로 넘어왔을 때의 월
        var holidayList : List<Map<String, Any>>? = null

        lifecycleScope.launch {
            try {
                holidayList = withContext(Dispatchers.IO) {
                    firestoreHelper.getMonthHolidaysFromFirestore(c_month)
                }
                // holidayList를 사용하여 UI에 데이터를 적용하는 작업 수행
                // 예를 들어, RecyclerView의 어댑터에 데이터를 설정하거나 화면에 출력
                var dates4 = mutableListOf<CalendarDay>()
                if (holidayList != null) {
                    for (holiday in holidayList!!) {
                        val year4 = holiday["start_year"].toString().toInt()

                        val month4 = holiday["start_month"].toString().toInt() + 1
                        val day4 = holiday["start_date"].toString().toInt()

                        val year5 = holiday["end_year"].toString().toInt()
                        val month5 = holiday["end_month"].toString().toInt() + 1
                        val day5 = holiday["end_date"].toString().toInt()

                        val startCalendarDay = CalendarDay.from(year4, month4, day4)
                        val endCalendarDay = CalendarDay.from(year5, month5, day5)

                        val daysInRange = getDatesInRange(startCalendarDay, endCalendarDay)
                        dates4.addAll(daysInRange)
                        val decorator = EventDecorator(HashSet(dates4))
                        binding.calendarview.addDecorator(decorator)
                    }
                }


            } catch (e: Exception) {
                // 예외 처리
                Log.e(ContentValues.TAG, "Error fetching holidays: $e")
            }
        }

        binding.calendarview.setOnDateChangedListener { widget, date, selected ->
            val year = date.year
            val month = date.month
            val day = date.day

            val year1 = date.year.toString()
            val month1 = if (date.month < 10) "0${date.month}" else date.month.toString()
            val day1 = if (date.day < 10) "0${date.day}" else date.day.toString()

            val combinedDate = "$year1$month1$day1" // 내가 클릭한 날

            val adapter = UpcomingSchedulesAdapter(
                mutableListOf(), mutableListOf(), mutableListOf(),
                mutableListOf(), mutableListOf(), mutableListOf(),
                mutableListOf(), mutableListOf()
            ) // 빈 어댑터 생성
            if (holidayList != null) {
                holidayList!!.forEach { holiday ->
                    val startYear = holiday["start_year"].toString()
                    val startMonth = holiday["start_month"] as Long
                    val startMonth1 = if (startMonth.toInt() < 10) "0${startMonth.toInt()}" else (startMonth.toInt()).toString()
                    val startDate = holiday["start_date"] as Long
                    val startDate1 = if (startDate.toInt() < 10) "0${startDate.toInt()}" else (startDate.toInt()).toString()
                    val start = "$startYear$startMonth1$startDate1" // 일정 시작 날짜

                    val endYear = holiday["end_year"].toString()
                    val endMonth = holiday["end_month"] as Long
                    val endMonth1 = if (endMonth.toInt() < 10) "0${endMonth.toInt()}" else (endMonth.toInt()).toString()
                    val endDate = holiday["end_date"] as Long
                    val endDate1 = if (endDate.toInt() < 10) "0${endDate.toInt()}" else (endDate.toInt()).toString()
                    val end = "$endYear$endMonth1$endDate1" // 일정 끝 날짜

                    // 클릭된 날짜가 해당 일정의 시작과 종료 날짜 사이에 있는지 확인
                    if(combinedDate.toInt() in start.toInt()..end.toInt()) {
                        adapter.datas_holidays_title.add(holiday["holiday_title"].toString())
                        adapter.datas_holidays_start_year.add(holiday["start_year"].toString())
                        adapter.datas_holidays_start_month.add(holiday["start_month"].toString())
                        adapter.datas_holidays_start_date.add(holiday["start_date"].toString())
                        adapter.datas_holidays_end_year.add(holiday["end_year"].toString())
                        adapter.datas_holidays_end_month.add(holiday["end_month"].toString())
                        adapter.datas_holidays_end_date.add(holiday["end_date"].toString())
                        adapter.datas_categories.add(holiday["category"].toString())
                    }
                }

                binding.scheduleRecycler.adapter = adapter // RecyclerView에 어댑터 설정
                binding.scheduleRecycler.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 설정
            }

            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("selectedYear", year)
            intent.putExtra("selectedMonth", month)
            intent.putExtra("selectedDay", day)
            startActivity(intent)
        }

        binding.calendarview.setOnMonthChangedListener { widget, date ->  // 달이 변경
            // 초기화
            binding.calendarview.removeDecorators()
            binding.calendarview.invalidateDecorators()

            binding.calendarview.addDecorators(
                TodayDecorator(),
                SatDecorator(),
                SunDecorator(),
                OtherMonth(date.month)
            )

            c_year = date.year // 현재 연도
            c_month = date.month // 현재 월

            lifecycleScope.launch {

                try {

                    holidayList = firestoreHelper.getMonthHolidaysFromFirestore(c_month)
                    // holidayList를 사용하여 UI에 데이터를 적용하는 작업 수행
                    // 예를 들어, RecyclerView의 어댑터에 데이터를 설정하거나 화면에 출력

                    var dates3 = mutableListOf<CalendarDay>()
                    if (holidayList != null) {
                        for (holiday in holidayList!!) {
                            val year4 = holiday["start_year"].toString().toInt()
                            val month4 = holiday["start_month"].toString().toInt() + 1
                            val day4 = holiday["start_date"].toString().toInt()

                            val year5 = holiday["end_year"].toString().toInt()
                            val month5 = holiday["end_month"].toString().toInt() + 1
                            val day5 = holiday["end_date"].toString().toInt()

                            val startCalendarDay = CalendarDay.from(year4, month4, day4)
                            val endCalendarDay = CalendarDay.from(year5, month5, day5)


                            val daysInRange = getDatesInRange(startCalendarDay, endCalendarDay)
                            dates3.addAll(daysInRange)
                            val decorator = EventDecorator(HashSet(dates3))
                            binding.calendarview.addDecorator(decorator)
                        }
                    }

                } catch (e: Exception) {
                    // 예외 처리
                    Log.e(ContentValues.TAG, "Error fetching holidays: $e")
                }
            }
        }
    }

    class EventDecorator(private val dates: HashSet<CalendarDay>) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(10F, Color.parseColor("#1D872A")))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDatesInRange(startDate: CalendarDay, endDate: CalendarDay): List<CalendarDay> {
        val datesInRange = mutableListOf<CalendarDay>()
        var currentDate = LocalDate.of(startDate.year, startDate.month, startDate.day)
        //val endLocalDate = LocalDate.of(endDate.year, endDate.month - 1, endDate.day)
        while (!currentDate.isAfter(LocalDate.of(endDate.year, endDate.month, endDate.day))) {
            datesInRange.add(CalendarDay.from(currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth))
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
            view?.addSpan(StyleSpan(Typeface.NORMAL))
            view?.addSpan(RelativeSizeSpan(1.0f))
        }
    }

    class SatDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.day?.let { dayOfMonth ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, day.year)
                    set(Calendar.MONTH, day.month - 1) // Calendar.MONTH는 0부터 시작하므로 1을 빼줍니다.
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

    class SunDecorator : DayViewDecorator {
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

