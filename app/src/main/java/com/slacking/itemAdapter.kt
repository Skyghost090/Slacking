package com.slacking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class itemAdapter (private val item_: ArrayList<item>) : RecyclerView.Adapter<itemViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return itemViewHolder(view)
    }
    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {

        val task_ = item_[position]
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, task_)
        }

        holder.bind(task_)
    }
    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, model: item)
    }

    override fun getItemCount(): Int {
        return item_.size
    }
}