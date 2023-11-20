package com.example.holymoly

import android.os.Build
import android.util.Log
import android.webkit.ValueCallback
import androidx.annotation.RequiresApi
import com.example.holymoly.NetworkThread3.Companion.convertToInt
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import javax.xml.parsers.DocumentBuilderFactory

interface HolyCallback {
    fun onValueReady(values: AllHolyData)
}

class HolyDay(val sol_year : String) : HolyCallback {
    private val key : String    //개인키(encoding)
    private var url : String    //API 정보를 가지고 있는 주소 : 공공데이터포털 - 특일정보
    private val thread : Thread //쓰레드
    private lateinit var allData : AllHolyData  //공휴일 전체 정보
    private val latch = CountDownLatch(1) // 스레드가 완료되기 전까지 대기 시간

    init{
        key = "f%2Fu%2Bs0YkJF139Kb6pvoySE7KYfgNQJwcVZNFUv%2BpTiOc%2FNODm7yH%2Bh4jAhDft5JkkY2Ca%2FT80gbOetgqn4U0SQ%3D%3D"
        url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear="+sol_year+"&numOfRows="+50+"&ServiceKey="+key

        //쓰레드 생성
        thread = Thread(NetworkThread3(url, this))
        thread.start()    //쓰레드 시작
        latch.await()    //멀티 작업 안되게 하기
    }

    //콜백을 통해 호출한 thread로부터 계산된 값 전달받기
    override fun onValueReady(values: AllHolyData) {
        allData = values

        // CountDownLatch를 감소시켜 쓰레드가 완료되었음을 알림
        latch.countDown()
    }

    //남은 공휴일 수 반환 함수
    fun restHolyOfYear() : Int {
        return allData.restHolyDayYear
    }

    //달 마다 남은 공휴일 수 반환
    @RequiresApi(Build.VERSION_CODES.O)
    fun restHolyOfMonth() : List<Int> {
        val month = LocalDate.now().monthValue
        var monthList = mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        monthList[month] = allData.restHolyDayMonth
        for (i in (month + 1)..13) {
            monthList[i] = allData.totalMonth[i]
        }

        return monthList
    }

    //이 달의 공휴일
    @RequiresApi(Build.VERSION_CODES.O)
    fun HolyListOfMonth() : List<List<String>>{
        var holyList = mutableListOf<List<String>>()
        val month = 12
        val day = LocalDate.now().dayOfMonth

        if(allData.holyDayYear.containsKey(month)){
        for (name in allData.holyDayYear[month]!!){
            val fullStart = allData.holyDayInform[name]?.start_date
            val fullEnd = allData.holyDayInform[name]?.end_date?:""
            val start = fullStart?.let { NetworkThread3.convertToInt(it.substring(6)) }
            if (start != null) {
                if(day <= start)
                {
                    var dateList = mutableListOf<String>()
                    dateList.add(name)
                    dateList.add(fullStart)
                    dateList.add(fullEnd)
                    holyList.add(dateList)
                }
            }
        }}
        Log.d("Success", holyList.toString())
        return holyList
    }


}

//HolyDay 클래스의 필요한 모든 정보 -> 콜백을 통해 값 전달을 위한 목적
data class AllHolyData(val totalYear : Int, val totalMonth : List<Int>,
                       val holyDayInform : Map<String, HolyDayData>, val holyDayYear : Map<Int, List<String>>,
                       val restHolyDayYear : Int, val restHolyDayMonth : Int, val observedCount : Int, val temporaryCount : Int)

//특정 공휴일 정보
data class HolyDayData(val start_date: String, val end_date: String, val total_day: Int)

//api 연결
class NetworkThread3 (private val url: String, private val callback: HolyCallback) : Runnable{
    ///////////필요한 정보////////////
    private var totalYear : Int = 0    //1년 공휴일 수
    private var totalMonth : MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0)   //월별 공휴일 수
    private var holyDayInform : MutableMap<String, HolyDayData> = mutableMapOf()   //공휴일 정보
    private var holyDayYear : MutableMap<Int, List<String>> = mutableMapOf()        //각 월에 속한 공휴일
    private var observedCount: Int = 0     //대체공휴일 수
    private var temporaryCount: Int = 0     //임시공휴일 수
    private var restOfMonth: Int = 0
    //앞에 0이 붙어져 있는 월, 일을 숫자로 바꾸기
    companion object {
        fun convertToInt(str : String) : Int
        {
            if(str[0] == '0')   return str[1]-'0'
            else    return str.toInt()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        try{
            ////////////////데이터 가져오기///////////////
            val xml : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            xml.documentElement.normalize()

            ///////////////1년의 총 공휴일 수//////////////
            totalYear = xml.getElementsByTagName("totalCount").item(0).textContent.toInt()

            //////////////각 특정 공휴일 데이터 저장하기////////////
            val list:NodeList = xml.getElementsByTagName("item")    //공휴일 정보 노드들
            var holyDayNames : MutableList<String> = mutableListOf()     //월별 공휴일 종류
            var temp : MutableList<String> = mutableListOf("")    //이전에 가져온 공휴일 데이터 리스트
            var total_date = 0       //특정 공휴일 기간
            var current_month : Int = 0  //현재 월

            //item노드의 리스트에서 각 item(일일공휴일) 가져오기
            for(i in 0..list.length-1){
                val n: Node = list.item(i)

                //노드가 존재할 경우
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    //각 리스트 정보 가져오기
                    val element = n as Element
                    val name = isSame(element.getElementsByTagName("dateName").item(0).textContent)
                    val start = element.getElementsByTagName("locdate").item(0).textContent

                    //현재 월 가져오기
                    current_month = convertToInt(start.substring(4,6))

                    //이전 월 정보 등록
                    if(totalMonth[current_month] == 0 && current_month > 1){
                        holyDayYear.put(convertToInt(temp[2].substring(4,6)), holyDayNames)
                        holyDayNames = mutableListOf()
                    }

                    //현재 달 총 공휴일 수 업데이트
                    totalMonth[current_month]++

                    //공휴일 이름별 정보 업데이트
                    if(temp[0] == name){
                        total_date++
                        temp[2] = start
                    }else{
                        holyDayNames.add(name)
                        if(temp[0] != "")
                            holyDayInform.put(temp[0], HolyDayData(temp[1], temp[2], total_date))
                        temp[0] = name
                        temp.add(1, start)
                        temp.add(2, start)
                        total_date = 1
                    }

                } }
            //나머지 자투리 데이터 처리
            if(list.length > 0){
                holyDayInform.put(temp[0], HolyDayData(temp[1], temp[2], total_date))
                if(holyDayNames.isNotEmpty())
                    holyDayYear[current_month] = holyDayNames
            }

            //콜백
            callback.onValueReady(AllHolyData(totalYear, totalMonth, holyDayInform, holyDayYear, restOfHoly(), restOfMonth, observedCount, temporaryCount))

            //공휴일 api 정보 확인하기
            Log.d("Success", totalYear.toString())
            for(i in holyDayInform.keys){
                val temp : HolyDayData? = holyDayInform[i]
                Log.d("Success", i + ": (" + temp?.total_day.toString() + ", " + temp?.start_date.toString() + ", " + temp?.end_date.toString() + ")")
            }
            Log.d("Success", holyDayYear.toString())
            Log.d("Success", restOfHoly().toString())
            Log.d("Success", restOfMonth.toString())

        }catch(e: Exception){ //api 에러
            Log.d("TTT", "오픈API"+e.toString())
        }
    }

    //대체공휴일, 임시공휴일 인지 아닌지
    private fun isSame(name : String) : String {
        val observe = "대체공휴일"
        val temporary = "임시공휴일"
        if(name.contains(observe)){
            observedCount++
            return observe + observedCount.toString()
        }
        else if(name.contains(temporary)){
            temporaryCount++
            return temporary + temporaryCount.toString()
        }
        else
            return name
    }

    //남은 공휴일 수(한 해)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun restOfHoly():Int{
        return totalYear - goneOfHoly()
    }

    //지난 공휴일 수(한 해)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun goneOfHoly():Int{
        val localDate = LocalDate.now()
        val month = localDate.monthValue
        val day = localDate.dayOfMonth
        var rest = 0
        restOfMonth = totalMonth[month]

        if(month > 1)
            for(i in 1 until month)
                rest += totalMonth[i]

        if(holyDayYear.containsKey(month)){
            for(i in holyDayYear[month]!!){
                val d = holyDayInform[i]?.start_date?.let { convertToInt(it.substring(6)) }
                if(d !! <= day){
                    for(j in 0 until (holyDayInform[i]?.total_day!!)){
                        if (j + d >= day)
                            return rest
                        else {
                            rest++
                            restOfMonth--
                        }
                    }}}}
        return rest
    }

}