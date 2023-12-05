package com.example.holymoly.ui.tab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.holymoly.databinding.BucketAddBinding

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
            val goal = binding.goal.text.toString() // 나의 목표 텍스트
            val memo = binding.memo.text.toString() // 메모 텍스트

            // db에 저장

            // 데이터 처리 끝나면 다시 이전 fragment로
            finish()
        }
    }
}