package com.example.holymoly.ui.tab


import android.content.Intent
import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.AddActivity
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentCalendarBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton.Size
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding
    val itemList = ArrayList<ScheduleItem>()
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
    }

    class TodayDecorator : DayViewDecorator {
        val date = CalendarDay.today()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            // 휴무일 || 이전 날짜
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

