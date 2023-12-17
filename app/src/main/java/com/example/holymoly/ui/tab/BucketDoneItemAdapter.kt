package com.example.holymoly.ui.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.databinding.BucketDoneItemBinding

interface CheckBucketDoneListener{
    fun onCheckBucketDone(flag: Boolean, pos: Int, time: String)
}

class BucketDoneItemHolder(val binding: BucketDoneItemBinding)
    : RecyclerView.ViewHolder(binding.root)

class BucketDoneItemAdapter ( )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var listener : CheckBucketDoneListener ?= null
    private var datas : List<BucketDoneInform> = mutableListOf()

    init{
        val fb = FirestoreHelper()
        datas = fb.getBucketDoneToFireStore(this)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as BucketDoneItemHolder).binding

        //버킷 내용
        binding.bucketTitle.text = datas[position].title
        binding.bucketContext.text = datas[position].context

        //체크박스
        val checkBtn = binding.bucketCheck
        checkBtn.setOnClickListener{//리스너 등록
            if(checkBtn.isChecked)
                listener?.onCheckBucketDone(true, position, datas[position].time)
            else
                listener?.onCheckBucketDone(false, position, datas[position].time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = BucketDoneItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BucketDoneItemHolder(binding)
    }

    fun setCheckDoneListener(listener: CheckBucketDoneListener){
        this.listener = listener
    }
}