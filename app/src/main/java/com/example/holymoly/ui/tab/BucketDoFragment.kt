package com.example.holymoly.ui.tab

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.R
import com.example.holymoly.databinding.BucketFragmentDoBinding

@RequiresApi(Build.VERSION_CODES.O)
class BucketDoFragment: Fragment() {
    lateinit var binding: BucketFragmentDoBinding
    private var bucketSelectedList = mutableSetOf<String>()
    private val fb = FirestoreHelper()
    //intent 돌아온 후 실행할 것
    /*private val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        bucketAdapter.notifyDataSetChanged()
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BucketFragmentDoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //리사이클러뷰 - 버킷
        val bucketAdapter = BucketItemAdapter()
        bucketAdapter.setCheckBucketListener(object : CheckBucketListener{
            override fun onCheckBucket(flag: Boolean, pos: Int, time: String) {
                if(flag) //삭제 리스트에 추가
                    bucketSelectedList.add(time)
                else     //삭제 리스트로부터 제거
                    bucketSelectedList.remove(time)
                Log.d("Success", "$pos : $flag")
            }
        })

        //버킷 어댑터 연결
        binding.bucketDoLayout.layoutManager = LinearLayoutManager(requireContext())
        binding.bucketDoLayout.adapter = bucketAdapter

        /*//새로 만들기 버튼(아무것도 없을 때)
        binding.addBucket.setOnClickListener{
            moveToActivity()
        }*/

        //새로 만들기 버튼
        binding.bucketAddBtn.setOnClickListener{
            moveToActivity()
        }

        //삭제 버튼
        binding.bucketDeleteBtn.setOnClickListener{
            if(bucketSelectedList.isEmpty()){
                alertNonePick()
            }else {
                fb.deleteBucketDoToFireStore(bucketSelectedList, bucketAdapter)
                //bucketSelectedList.clear()
            }
        }

        //시작 버튼
        binding.bucketStartBtn.setOnClickListener{
            if(bucketSelectedList.isEmpty()) {
                alertNonePick()
            }else{
                alertProcessDialog(bucketAdapter)
                //bucketSelectedList.clear()
            }
        }

        //종료 버튼
        binding.bucketFinishBtn.setOnClickListener{
            if(bucketSelectedList.isEmpty()) {
                alertNonePick()
            }else{
                alertFinishDialog(bucketAdapter)
                //bucketSelectedList.clear()
            }
        }

    }

    //버킷 작성 액티비티 띄우기
    fun moveToActivity(){
        val intent = Intent(context, AddBucket::class.java)
        startActivity(intent)
    }

    //시작 확인 다이얼로그 띄우기
    fun alertProcessDialog(bucketAdapter : BucketItemAdapter){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.bucket_alert_start_question)

        val listener = object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, btn: Int) {
                when(btn){
                    DialogInterface.BUTTON_POSITIVE -> fb.modifyBucketDoToFireStore(bucketSelectedList, bucketAdapter)
                    DialogInterface.BUTTON_NEGATIVE -> Toast.makeText(requireContext(), R.string.bucket_alert_start_cancel, Toast.LENGTH_SHORT).show()
                }
            }
        }

        //버튼 리스너 등록
        builder.setPositiveButton(R.string.bucket_alert_yes, listener)
        builder.setNegativeButton(R.string.bucket_alert_no, listener)

        builder.show()
    }

    //종료 확인 다이얼로그 띄우기
    fun alertFinishDialog(bucketAdapter : BucketItemAdapter){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.bucket_alert_finish_question)

        val listener = object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, btn: Int) {
                when(btn){
                    DialogInterface.BUTTON_POSITIVE -> {
                        fb.addBucketDoneToFireStore(bucketSelectedList, bucketAdapter)
                        Toast.makeText(requireContext(), R.string.bucket_alert_finish_complete, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //버튼 리스너 등록
        builder.setPositiveButton(R.string.bucket_alert_yes, listener)
        builder.setNegativeButton(R.string.bucket_alert_no, listener)

        builder.show()
    }

    //아무 체크박스도 선택하지 않았을 때
    fun alertNonePick(){
        Toast.makeText(requireContext(), R.string.bucket_alert_none_item, Toast.LENGTH_SHORT).show()
    }
}
