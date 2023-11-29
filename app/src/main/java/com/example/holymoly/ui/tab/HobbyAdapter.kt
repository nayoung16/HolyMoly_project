package com.example.holymoly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.databinding.MdHobbyItemviewBinding

class MyHobbyViewHolder(val binding: MdHobbyItemviewBinding)
    : RecyclerView.ViewHolder(binding.root)

class HobbyAdapter (val datas : List<Int>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyHobbyViewHolder).binding
        val resources = holder.itemView.context.resources
        val textID = resources.getIdentifier("md_hobby_$position", "string", "com.example.holymoly")
        val photoID = resources.getIdentifier("hobby_$position", "drawable", "com.example.holymoly")

        binding.mdTravelBtnText.setText(textID)
        binding.mdTravelBtnImage.setImageResource(photoID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = MdHobbyItemviewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyHobbyViewHolder(binding)
    }

}