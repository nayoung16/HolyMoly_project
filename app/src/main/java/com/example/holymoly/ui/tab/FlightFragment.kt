package com.example.holymoly.ui.tab

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.R
import com.example.holymoly.databinding.FragmentFlightBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class FlightFragment : Fragment(), OnCountryItemSelectedListener {
    private lateinit var binding : FragmentFlightBinding
    private val calendar = Calendar.getInstance()
    private var departCountry : String = ""
    private var arriveCountry : String = ""
    private var departDate : String = ""
    private var arriveDate : String = ""
    private var minDate : Calendar = calendar
    private val fireStore = FirestoreHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFlightBinding.inflate(inflater, container, false)


        //나라 선택 어댑터
        val countryList = listOf("ICN", "CJU", "PUS", "TAE", "GMP", "RSU", "USN", "TYO", "DAD", "CDG", "JFK", "GUM", "PEK", "CEB", "TPE", "MEL")
        //출발 나라
        val countryAdapterFrom = FlightyCountryAdapter(requireContext(), binding.flightyFrom, countryList)
        countryAdapterFrom.setOnCountryItemSelectedListener(this)
        //도착 나라
        val countryAdapterTo = FlightyCountryAdapter(requireContext(), binding.flightyTo, countryList)
        countryAdapterTo.setOnCountryItemSelectedListener(this)

        //날짜 선택
        binding.departBtn.setOnClickListener{
            getFlightDate(binding.departDate) }
        binding.arriveBtn.setOnClickListener{
            getFlightDate(binding.arriveDate) }

        //비행기 조회
        binding.flightySearchBtn.setOnClickListener {
            if(check()){
                setWebView()
            }
        }

        //검색 내역 저장
        binding.flightyStoreBtn.setOnClickListener{
            if(check())
                storeTicket()
        }

        return binding.root
    }

    //검색 내역 저장 - fireStore
    private fun storeTicket(){
        val type = getFlightType()
        fireStore.storeTicketToFireStore(
            makeTimeStamp(), type, departCountry, arriveCountry,
            binding.departDate.text.toString(), binding.arriveDate.text.toString()
        )

        Toast.makeText(requireContext(), "해당 검색 내역이 저장되었습니다:)", Toast.LENGTH_SHORT).show()
    }

    //fireStore에 저장할 키 생성
    private fun makeTimeStamp(): String{
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")
        return now.format(formatter).toString()
    }

    //올바른 입력 형식 검사
    private fun check() : Boolean {
        //출발지와 목적지를 같게 설정했다면
        if(departCountry == arriveCountry){
            Toast.makeText(requireContext(), "출발지와 목적지는 다르게 설정해주세요!", Toast.LENGTH_SHORT).show()
            return false
        }
        //날짜를 입력하지 않았다면
        else if(departDate == "" || arriveDate == ""){
            Toast.makeText(requireContext(), "날짜를 입력해주세요!", Toast.LENGTH_SHORT).show()
            return false
        }
        //제대로 입력했다면
        else
            return true
    }
    //웹뷰 띄우기
    private fun setWebView() {
        val intent = Intent(requireContext(), FlightyWebView()::class.java)
        val type = getFlightType()
        intent.putExtra("type", type)
        intent.putExtra("departCountry", departCountry)
        intent.putExtra("arriveCountry", arriveCountry)
        intent.putExtra("departDate", departDate)
        intent.putExtra("arriveDate", arriveDate)
        startActivity(intent)
    }

    //출발, 도착 나라 값 가져오기
    override fun onCountryItemSelected(countryItem: String, spinner: Spinner) {
        when(spinner){
            binding.flightyFrom -> departCountry = countryItem
            binding.flightyTo -> arriveCountry = countryItem
        }
    }

    //국제선 or 국내선 type 확인
    private fun getFlightType() : String {
        //국내선 리스트
        val domList = setOf("CJU", "PUS", "TAE", "ICN", "GMP", "RSU", "USN")
        return if(arriveCountry in domList && departCountry in domList) "domestic" else  "international"
    }

    //여행 날짜 가져오기
    private fun getFlightDate(view: TextView){
            //버튼에 따른 리스너 등록
        val listener = DatePickerDialog.OnDateSetListener{
            _, year, month, day ->
            run {
                val monthStr = convertToStr(month+1)
                val dayStr = convertToStr(day)
                val date = "${year}"+ monthStr + dayStr //쿼리문에 넣을 내용
                val dateStr = "${year}/$monthStr/$dayStr" //텍스트뷰에 보여질 내용
                when (view?.id) {
                    R.id.depart_date -> { //출발
                        view.text = dateStr
                        departDate = date
                        //출발 날짜를 바꾸면 도착 날짜 초기화
                        binding.arriveDate.text = ""
                        arriveDate = ""
                        minDate = Calendar.getInstance().apply{set(year, month, day)}
                    }

                    R.id.arrive_date -> {  //도착
                        view.text = dateStr
                        arriveDate = date
                    }
                }
            }
        }
        //다이얼로그 생성
        val dialog = DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        //최소 날짜 : 현재
        dialog.datePicker.minDate = if(view.id == R.id.depart_date) calendar.timeInMillis else minDate?.timeInMillis!!
        dialog.show()
    }

    private fun convertToStr(num : Int): String{
        if(num < 10)
            return "0$num"
        else
            return "$num"
    }
}