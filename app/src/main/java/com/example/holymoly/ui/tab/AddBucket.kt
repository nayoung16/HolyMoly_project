package com.example.holymoly.ui.tab

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.databinding.BucketAddBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class AddBucket : AppCompatActivity() {     // 버킷리스트 추가 액티비티
    lateinit var binding : BucketAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BucketAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goback.setOnClickListener { // 삭제 버튼 클릭
            finish()
        }

        binding.savebucket.setOnClickListener{  // 저장 버튼 클릭
            val title = binding.goal.text.toString() // 나의 목표 텍스트
            val memo = binding.memo.text.toString() // 메모 텍스트

            // db에 저장
            val fb = FirestoreHelper()
            fb.addBucketDoToFireStore(makeTimeStamp(), title, memo, false)

            // 데이터 처리 끝나면 다시 이전 fragment로
            finish()
        }
    }

    private fun makeTimeStamp(): String{
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")
        return now.format(formatter).toString()
    }
}