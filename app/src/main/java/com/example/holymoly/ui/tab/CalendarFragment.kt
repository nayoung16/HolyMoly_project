package com.example.holymoly.ui.tab

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.AddActivity
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentCalendarBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.Calendar

class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 선택된 달 값 받아오기
        val selectedMonthIndex = arguments?.getInt("selectedMonthIndex", 0) ?: 0
        // binding 초기화
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, selectedMonthIndex) // 선택된 달로 이동
            set(Calendar.DAY_OF_MONTH, 1) // 해당 월의 첫 번째 날로 설정
        }

        //val selectedCalendarDay = CalendarDay.from(calendar)

        //val materialCalendarView = binding.calendarview
        //materialCalendarView.setCurrentDate(selectedCalendarDay)
        // Inflate the layout for this fragment
        return binding.root
    }

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

        val titles = listOf("Title 1", "Title 2", "Title 3") // 예시 데이터
        val details = listOf("Detail 1", "Detail 2", "Detail 3") // 예시 데이터
        val images = listOf(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground) // 예시 데이터

        val adapter = UpcomingSchedulesAdapter(titles, details, images) // 어댑터 생성 및 데이터 전달

        binding.scheduleRecycler.adapter = adapter // RecyclerView에 어댑터 설정
        binding.scheduleRecycler.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 설정

        var c_year = CalendarDay.today().year   // 캘린더 화면으로 넘어왔을 때의 년도
        var c_month = CalendarDay.today().month // 캘린더 화면으로 넘어왔을 때의 월


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

        }
        binding.calendarview.setOnDateChangedListener { widget, date, selected ->
            val year = date.year
            val month = date.month
            val day = date.day

            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("selectedYear", year)
            intent.putExtra("selectedMonth", month)
            intent.putExtra("selectedDay", day)
            startActivity(intent)

        }

        // Firestore 초기화
        var firestore = FirebaseFirestore.getInstance()

        // Firestore에서 일정 데이터 가져오기
        /*firestore.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                val eventDates = HashSet<CalendarDay>()
                for (document in documents) {
                    val eventDate = document.getString("date")

                    eventDate?.let {
                        val year = it[0].toInt()
                        val month = it[1].toInt() - 1
                        val day = it[2].toInt()
                        val calendarDay = CalendarDay.from(year, month, day)
                        eventDates.add(calendarDay)
                        binding.calendarview.addDecorator(
                            EventDecorator(
                                Color.parseColor("#0E406B"),
                                Collections.singleton(CalendarDay.from(year, month - 1, day))
                            )
                        )
                    }
                }
            }*/

    }

    class EventDecorator(dates: Collection<CalendarDay>): DayViewDecorator {    // 일정이 있으면 점 찍기
        var dates: HashSet<CalendarDay> = HashSet(dates)

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
        }
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

