package com.example.holymoly.ui.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.R
import com.example.holymoly.databinding.BucketItemBinding

interface CheckBucketListener{
    fun onCheckBucket(flag: Boolean, pos: Int, time: String)
}

/*interface DataChangedListener{
    fun onDataChanged(flag: Boolean)
}*/

class BucketItemHolder(val binding: BucketItemBinding)
    : RecyclerView.ViewHolder(binding.root)

class BucketItemAdapter ( )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var listener : CheckBucketListener ?= null
    //private var changeListener : DataChangedListener ?= null
    private var datas : List<BucketInform> = mutableListOf()

    init{
        val fb = FirestoreHelper()
        datas = fb.getBucketDoToFireStore(this)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as BucketItemHolder).binding

        //버킷 내용
        binding.bucketTitle.text = datas[position].title
        binding.bucketContext.text = datas[position].context

        //버킷 진행 버튼
        val processBtn = binding.bucketProcess
        val resource = holder.itemView.context.resources
        if(datas[position].process){
            processBtn.setBackgroundResource(resource.getIdentifier("bucket_process_btn2", "drawable", "com.example.holymoly"))
            binding.processText.setText(R.string.bucket_item_process_true)
        }else{
            processBtn.setBackgroundResource(resource.getIdentifier("bucket_process_btn1", "drawable", "com.example.holymoly"))
            binding.processText.setText(R.string.bucket_item_process_false)
        }

        //체크박스
        val checkBtn = binding.bucketCheck
        checkBtn.setOnClickListener{//리스너 등록
            if(checkBtn.isChecked)
                listener?.onCheckBucket(true, position, datas[position].time)
            else
                listener?.onCheckBucket(false, position, datas[position].time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = BucketItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BucketItemHolder(binding)
    }

    fun setCheckBucketListener(listener: CheckBucketListener){
        this.listener = listener
    }

    /*fun setDataChangedListener(listener: DataChangedListener){
        this.changeListener = listener
    }*/
}