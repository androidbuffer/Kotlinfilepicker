package com.androidbuffer.kotlinfilepickersample

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.androidbuffer.kotlinfilepicker.KotResult

/**
 * Created by AndroidBuffer on 24/6/18.
 */

class ThumbnailAdapter(listOfImages: ArrayList<KotResult>,listener:OnThumbnailListener) : RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>() {

    val localList = listOfImages
    val clickListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageURI(localList.get(position).uri)
    }

    override fun getItemCount(): Int {
        return localList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.ivThumbnail)
            imageView.setOnClickListener((View.OnClickListener {
                clickListener.onThumbnailClick(adapterPosition)
            }))
        }
    }

    /**
     * interface for onClick listener
     */
    interface OnThumbnailListener {
        fun onThumbnailClick(position: Int)
    }
}
