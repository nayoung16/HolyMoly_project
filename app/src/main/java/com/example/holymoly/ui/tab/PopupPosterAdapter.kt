package com.example.holymoly.ui.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holymoly.databinding.PopupItemviewBinding

class PosterViewHolder(val binding: PopupItemviewBinding)
    : RecyclerView.ViewHolder(binding.root)

/*대상 객체에 따라 바뀜 -> 제너릭*/
class PopupPosterAdapter (val flag: Int, val month: Int)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        if(flag == 0){ return 2 }
        else { return 5 }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as PosterViewHolder).binding

        val resources = holder.itemView.context.resources
        var photoID : Int = 0
        var scriptID : Int = 0
        var titleID : Int = 0

        when(flag){
            0 -> {
                photoID = resources.getIdentifier("trip${month}_img_${position+1}", "drawable", "com.example.holymoly")
                scriptID = resources.getIdentifier("popup_trip${month}_${position+1}_script", "string", "com.example.holymoly")
                titleID = resources.getIdentifier("popup_trip${month}_${position+1}_title", "string", "com.example.holymoly")
            }
            1 -> {
                photoID = resources.getIdentifier("movie_poster_${position+1}", "drawable", "com.example.holymoly")
                scriptID = resources.getIdentifier("popup_movie${position+1}_script", "string", "com.example.holymoly")
                titleID = resources.getIdentifier("popup_movie${position+1}_title", "string", "com.example.holymoly")
            }
            2 -> {
                photoID = resources.getIdentifier("book_img_${position+1}", "drawable", "com.example.holymoly")
                scriptID = resources.getIdentifier("popup_book${position+1}_script", "string", "com.example.holymoly")
                titleID = resources.getIdentifier("popup_book${position+1}_title", "string", "com.example.holymoly")
            }
        }

        binding.movieImg.setImageResource(photoID)
        binding.movieScript.setText(scriptID)
        binding.movieTitle.setText(titleID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder = PosterViewHolder(PopupItemviewBinding.inflate(LayoutInflater.from(parent.context),parent, false))

}