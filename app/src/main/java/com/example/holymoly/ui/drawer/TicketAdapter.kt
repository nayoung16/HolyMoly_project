package com.example.holymoly.ui.drawer

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.databinding.MyFlightTicketItemBinding

data class TicketInform(val time: String, val type: String,
                        val departDate: String, val arriveDate: String,
                        val departCountry: String, val arriveCountry: String)

interface SelectedTicketListener{
    fun onCheckTicket(flag: Boolean, pos: Int, time: String)
}

class TicketViewHolder(val binding: MyFlightTicketItemBinding)
    : RecyclerView.ViewHolder(binding.root)

class TicketAdapter ( )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var listener : SelectedTicketListener ?= null
    private var datas : List<TicketInform> = mutableListOf()

    init{
        val fb = FirestoreHelper()
        datas = fb.getTicketFromFireStore(this)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as TicketViewHolder).binding

        //티켓 정보 나타내기
        binding.ticketDepartDate.text = datas[position].departDate
        binding.ticketArriveDate.text = datas[position].arriveDate
        binding.ticketDepartCountry.text = datas[position].departCountry
        binding.ticketArriveCountry.text = datas[position].arriveCountry

        //삭제 체크 박스
        val btn = binding.ticketDeleteBtn
        btn.setOnClickListener{//리스너 등록
            if(btn.isChecked)
                listener?.onCheckTicket(true, position, datas[position].time)
            else
                listener?.onCheckTicket(false, position, datas[position].time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = MyFlightTicketItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return TicketViewHolder(binding)
    }

    fun setSelectedTicketListener(listener: SelectedTicketListener){
        this.listener = listener
    }
}