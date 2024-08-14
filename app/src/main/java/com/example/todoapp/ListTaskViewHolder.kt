package com.example.todoapp

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ListTaskViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    private val tvDescription: TextView = view.findViewById(R.id.tvDescription)
    private val imgComplete: ImageView = view.findViewById(R.id.imgvComplete)
    private val crdTask: MaterialCardView = view.findViewById(R.id.crdTask)
    fun render(
        tarea: Task,
        checkedListener: (ImageView, boleano: Boolean, iden: Int) -> Unit,
        viewTask: (id: Int) -> Unit,
        pressedCard: (id: Int) -> Unit
    ){
        tvTitle.text = tarea.titleTask
        tvDescription.text = tarea.description
        if (tarea.state){
            imgComplete.setColorFilter(ContextCompat.getColor(imgComplete.context, R.color.complete_task));
        }else{
            imgComplete.setColorFilter(ContextCompat.getColor(imgComplete.context, R.color.incomplete_task));
        }

        imgComplete.setOnClickListener {
            checkedListener(imgComplete, tarea.state, tarea.id)
        }
        crdTask.setOnClickListener {
            viewTask(tarea.id)
        }
        crdTask.setOnLongClickListener {
            Log.d("TAG", "onBindViewHolder: se preciono")
            pressedCard(tarea.id)
            true
        }
    }
}