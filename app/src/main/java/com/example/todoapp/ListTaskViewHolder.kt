package com.example.todoapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ListTaskViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    private val tvDescription: TextView = view.findViewById(R.id.tvDescription)
    private val imgComplete: ImageView = view.findViewById(R.id.imgvComplete)
    private val crdTask: MaterialCardView = view.findViewById(R.id.crdTask)
    fun render(tarea: Task, checkedListener: (ImageView) -> Unit, viewTask: (id: Int) -> Unit){
        tvTitle.text = tarea.titleTask
        tvDescription.text = tarea.description
        imgComplete.setOnClickListener {
            checkedListener(imgComplete)
        }
        crdTask.setOnClickListener {
            viewTask(tarea.id)
        }
    }
}