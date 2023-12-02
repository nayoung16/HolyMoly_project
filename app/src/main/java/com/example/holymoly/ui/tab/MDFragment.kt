package com.example.holymoly.ui.tab

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.HobbyAdapter
import com.example.holymoly.R
import com.example.holymoly.SetOnTravelItemClickListener
import com.example.holymoly.TravelCountryAdapter
import com.example.holymoly.databinding.FragmentMDBinding
import java.time.LocalDate
@RequiresApi(Build.VERSION_CODES.O)
class MDFragment : Fragment() {
    val month = LocalDate.now().monthValue
    private lateinit var binding : FragmentMDBinding
    private val hobbyAdapter = HobbyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMDBinding.inflate(inflater, container, false)

        //여행지 정보 어댑터 연결
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.mdTravelLayout.layoutManager = layoutManager
        val travelAdapter = TravelCountryAdapter()
        binding.mdTravelLayout.adapter = travelAdapter
        travelAdapter.TravelItemClick(object : SetOnTravelItemClickListener{  //리스너 함수 등록
            override fun showPopupTravel() {
                PopupMD(requireContext()).show()
            }
        })
        layoutManager.scrollToPosition(month-1) //현재 달에 대한 정보를 가운데로 배치

        //hobby 어댑터 연결
        binding.mdHobbyLayout.layoutManager = GridLayoutManager(activity, 3)
        binding.mdHobbyLayout.adapter = hobbyAdapter

        //더보기 버튼 리스너 등록
        binding.mdExtraButton.setOnClickListener{
            extraButtonClick()
        }

        //책 추천 리스너 등록
        binding.mdBookBtn.setOnClickListener{
            showPopupBook()
        }

        //영화 추천 리스너 등록
        binding.mdMovieBtn.setOnClickListener{
            showPopupMovie()
        }

        return binding.root
    }


    // 랜덤 hobby 추천을 위한 set 생성
    fun getRandomHobbySet() : Set<Int> {
        var hobbyData : MutableSet<Int> = mutableSetOf<Int>()
        while(hobbyData.size != 12){
            val index = (1..25).random()
            hobbyData.add(index)
        }

        return hobbyData
    }

    private fun extraButtonClick(){
        if(HobbyAdapter.show){
            binding.mdExtraButton.setImageResource(R.drawable.md_extrabtn_plus)
            HobbyAdapter.show = false
        }else{
            binding.mdExtraButton.setImageResource(R.drawable.md_extrabtn_minus)
            HobbyAdapter.show = true
        }
        hobbyAdapter.notifyDataSetChanged()
    }

    private fun showPopupBook() {
        PopupMD(requireContext()).show()
    }

    private fun showPopupMovie(){
        PopupMD(requireContext()).show()
    }
}