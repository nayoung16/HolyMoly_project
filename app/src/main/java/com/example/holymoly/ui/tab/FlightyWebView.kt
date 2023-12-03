package com.example.holymoly.ui.tab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.example.holymoly.databinding.ActivityFlightyWebViewBinding

class FlightyWebView() : AppCompatActivity() {
    private val binding by lazy{ActivityFlightyWebViewBinding.inflate(layoutInflater)}
    private var url = "https://flight.naver.com/flights"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //웹뷰 세팅
        binding.flightyWebview.apply{
            //페이지 컨트롤을 위한 기본적인 함수, 다양한 요청, 알람을 수신하는 기능
            webViewClient = WebViewClient()
            //크롬 환경에 맞는 세팅을 해줌
            webChromeClient = WebChromeClient()
            //자바스크립트 허용
            settings.javaScriptEnabled = true
        }

        //웹뷰 연결
        binding.flightyWebview.loadUrl(makeUrl())

        //back 버튼 리스너 등록
        binding.flightyBackBtn.setOnClickListener{
            onBackBtnPressed()
        }
    }

    private fun onBackBtnPressed(){
        finish()
    }

    override fun onBackPressed() {
        val view = binding.flightyWebview
        if(view.canGoBack())
            view.goBack()
        else
            finish()
    }

    private fun makeUrl(): String{
        val type = intent.getStringExtra("type")
        val departCountry = intent.getStringExtra("departCountry")
        val arriveCountry = intent.getStringExtra("arriveCountry")
        val departDate = intent.getStringExtra("departDate")
        val arriveDate = intent.getStringExtra("arriveDate")

        url += "/$type/$departCountry-$arriveCountry-$departDate"
        url += "/$arriveCountry-$departCountry-$arriveDate"
        url += "?adult=1&fareType=YC"

        Log.d("Success", url)
        return url
    }
}