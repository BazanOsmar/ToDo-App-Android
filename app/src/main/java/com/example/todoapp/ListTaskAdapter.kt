package com.example.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ListTaskAdapter(var taks: List<Task>, val checkedListener:(ImageView, boleano:Boolean, iden:Int)->Unit, val pressedCard:(id:Int)->Unit, val viewTask:(id:Int)->Unit): RecyclerView.Adapter<ListTaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_card, parent, false)
        return ListTaskViewHolder(view)
    }

    override fun getItemCount() = taks.size

    override fun onBindViewHolder(holder: ListTaskViewHolder, position: Int) {
        holder.render(taks[position], checkedListener, viewTask, pressedCard)

    }
}