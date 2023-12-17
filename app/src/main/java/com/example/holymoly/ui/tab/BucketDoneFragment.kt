package com.example.holymoly.ui.tab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.R
import com.example.holymoly.databinding.BucketFragmentDoneBinding

class BucketDoneFragment: Fragment() {
    lateinit var binding: BucketFragmentDoneBinding
    private var doneSelectedList = mutableSetOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BucketFragmentDoneBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //리사이클러뷰 - 버킷
        val bucketAdapter = BucketDoneItemAdapter()
        bucketAdapter.setCheckDoneListener(object : CheckBucketDoneListener{
            override fun onCheckBucketDone(flag: Boolean, pos: Int, time: String) {
                if(flag) //삭제 리스트에 추가
                    doneSelectedList.add(time)
                else     //삭제 리스트로부터 제거
                    doneSelectedList.remove(time)
                Log.d("Success", "$pos : $flag")
            }
        })

        //버킷 어댑터 연결
        binding.bucketDoneLayout.layoutManager = LinearLayoutManager(requireContext())
        binding.bucketDoneLayout.adapter = bucketAdapter

        binding.bucketDeleteBtn.setOnClickListener{
            if(doneSelectedList.isEmpty()){
                alertNonePick()
            }else{
                val fb = FirestoreHelper()
                fb.deleteBucketDoneToFireStore(doneSelectedList, bucketAdapter)
            }
        }
    }
    fun alertNonePick(){
        Toast.makeText(requireContext(), R.string.bucket_alert_none_item, Toast.LENGTH_SHORT).show()
    }
}