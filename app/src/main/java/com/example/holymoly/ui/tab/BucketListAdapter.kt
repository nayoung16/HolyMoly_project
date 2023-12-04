package com.example.holymoly.ui.tab

import android.speech.RecognitionListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.FirestoreHelper
import com.example.holymoly.databinding.ItemBucketListBinding

data class BucketInform(val title: String, val subTitle: String)
class BucketListViewHolder (val binding: ItemBucketListBinding) : RecyclerView.ViewHolder(binding.root)
class BucketListAdapter () :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var datas : List<BucketInform> = mutableListOf()
    init {
        val fb = FirestoreHelper()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}