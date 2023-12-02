package com.example.holymoly.ui.tab

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.example.holymoly.databinding.ActivityPopupMdBinding

class PopupMD(context: Context) : Dialog(context) {
    private lateinit var binding: ActivityPopupMdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopupMdBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //팝업창 크기 조절
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = layoutParams

        //팝업창 옵션
        initViews()
    }

    private fun initViews() = with(binding){
        //뒤로가기 버튼, 빈 화면 터치를 통해 다이얼로그가 사라지지 않도록 함
        setCancelable(false)

        //뷰 페이저 어댑터 연결
        val adapter = PopupPosterAdapter()
        binding.mdPopupViewpager.adapter = adapter

        //버튼 리스너 등록
        binding.viewpageNextbtn.setOnClickListener{//다음 버튼
            val currentItem = binding.mdPopupViewpager.currentItem
            if(currentItem < adapter.itemCount - 1)
                binding.mdPopupViewpager.setCurrentItem(currentItem+1, true)
        }

        binding.viewpageBackbtn.setOnClickListener {//이전 버튼
            val currentItem = binding.mdPopupViewpager.currentItem
            if(currentItem >= 0)
                binding.mdPopupViewpager.setCurrentItem(currentItem-1, true)
        }

        binding.popupBackbtn.setOnClickListener {//창 닫기
            dismiss()
        }
    }
}