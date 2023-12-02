package com.example.holymoly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.databinding.MdTravelItemviewBinding

interface SetOnTravelItemClickListener{
    fun showPopupTravel()
}
class MyTravelViewHolder(val binding: MdTravelItemviewBinding)
    : RecyclerView.ViewHolder(binding.root)

class TravelCountryAdapter ( )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onTravelItemClickListener: SetOnTravelItemClickListener ?= null
    override fun getItemCount(): Int = 12

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyTravelViewHolder).binding
        val resources = holder.itemView.context.resources
        val textID = resources.getIdentifier("md_travel_${position+1}", "string", "com.example.holymoly")
        val photoID = resources.getIdentifier("travel_country${position+1}", "drawable", "com.example.holymoly")

        binding.mdTravelBtnText.setText(textID)
        binding.mdTravelBtnImage.setImageResource(photoID)

        //클릭 리스너 등록
        binding.root.setOnClickListener{
            onTravelItemClickListener?.showPopupTravel()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = MdTravelItemviewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyTravelViewHolder(binding)
    }

    fun TravelItemClick(click : SetOnTravelItemClickListener){
        this.onTravelItemClickListener = click
    }

}