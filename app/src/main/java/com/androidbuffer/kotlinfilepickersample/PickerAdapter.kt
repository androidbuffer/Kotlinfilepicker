package com.androidbuffer.kotlinfilepickersample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by AndroidBuffer on 26/1/18.
 */
class PickerAdapter(titleArray: Array<String>,
                    drawableArray: Array<Int>,
                    clickItemListener: OnClickItemListener) : RecyclerView.Adapter<PickerAdapter.ViewHolder>() {

    val arrayList: Array<String> = titleArray
    val drawableList: Array<Int> = drawableArray
    val itemClickListener: OnClickItemListener = clickItemListener

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //map values to views here
        holder.tvTitle.text = arrayList[position]
        holder.ivTitleIcon.setImageResource(drawableList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_row_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return arrayList.size;
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        //here goes view holder
        var tvTitle: TextView
        var ivTitleIcon: ImageView

        init {
            //set the item click listener
            tvTitle = itemView.findViewById(R.id.tvTitle)
            ivTitleIcon = itemView.findViewById(R.id.ivTitleIcon)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View) {
            //handle the click listener on items
            itemClickListener.onItemClick(adapterPosition)
        }
    }

    interface OnClickItemListener {
        fun onItemClick(position: Int)
    }
}