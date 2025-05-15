package com.slacking

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class itemViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(tasks_: item) {
        itemView.findViewById<TextView>(R.id.taskName).text = tasks_.name
        itemView.findViewById<TextView>(R.id.taskInstructions).text = tasks_.description_
    }
}