package com.example.holymoly.ui.drawer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.databinding.FragmentMyFlightBinding

class MyFlightFragment : Fragment() {
    private var ticketSelectedList : MutableSet<String> = mutableSetOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMyFlightBinding.inflate(inflater, container, false)

        //티켓 어댑터
        val ticketAdapter = TicketAdapter( )
        binding.ticketLayout.layoutManager = LinearLayoutManager(activity)
        binding.ticketLayout.adapter = ticketAdapter

        //체크박스 리스너 호출
        ticketAdapter.setSelectedTicketListener(object : SelectedTicketListener{
            override fun onCheckTicket(flag: Boolean, pos: Int, time: String) {
                if(flag) //삭제 리스트에 추가
                    ticketSelectedList.add(time)
                else     //삭제 리스트로부터 제거
                    ticketSelectedList.remove(time)

                Log.d("Success", "$pos : $flag")
            }
        })

        //체크된 티켓들 삭제
        binding.selectedDeleteBtn.setOnClickListener{
            deleteTickets(ticketAdapter)
        }

        return binding.root
    }

    fun deleteTickets(adapter: TicketAdapter){
        if(ticketSelectedList.isEmpty())
            Toast.makeText(requireContext(), "선택된 티켓이 없습니다!", Toast.LENGTH_SHORT).show()
        else{
            val fb = FirestoreHelper()
            fb.deleteTicketFromFireStore(ticketSelectedList, adapter)
        }
        ticketSelectedList.clear()
    }
}