package com.example.holymoly

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val calendarImg:ImageView = findViewById(R.id.calendar_image)
        calendarImg.setColorFilter(Color.parseColor("#FFFBFB"))

        // 타이머가 끝나면 내부 실행
        Handler().postDelayed(Runnable {
            val user = Firebase.auth.currentUser
            if (user != null) {
                // 사용자가 로그인되어 있으면 MainActivity로 이동


                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                // 사용자가 로그인되어 있지 않으면 AuthActivity로 이동
                val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                startActivity(intent)
            }
            // 현재 액티비티 닫기
            finish()
        }, 2000) // 3초


    }
}