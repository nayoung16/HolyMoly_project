package com.example.holymoly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.databinding.MdHobbyItemviewBinding

class MyHobbyViewHolder(val binding: MdHobbyItemviewBinding)
    : RecyclerView.ViewHolder(binding.root)
class HobbyAdapter ( )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object {
        var show = false
    }
    private val showCount = 3

    override fun getItemCount(): Int {
        return if(show)
            9
        else
            3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyHobbyViewHolder).binding
        val resources = holder.itemView.context.resources
        val textID = resources.getIdentifier("md_hobby_1", "string", "com.example.holymoly")
        val photoID = resources.getIdentifier("hobby_1", "drawable", "com.example.holymoly")

        binding.mdTravelBtnText.setText(textID)
        binding.mdTravelBtnImage.setImageResource(photoID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = MdHobbyItemviewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyHobbyViewHolder(binding)
    }

}