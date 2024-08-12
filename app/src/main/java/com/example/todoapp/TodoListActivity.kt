package com.example.todoapp

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.todoapp.databinding.ActivityTodoListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodoListActivity : AppCompatActivity() {
    companion object{
        const val BANDERA = "banderaTask"
        const val ID = "idTask"
    }
    //Comentario para borrar
    private lateinit var binding: ActivityTodoListBinding
    private lateinit var room: TaskDatabase
    private lateinit var taskAdapter: ListTaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Borrar base de datos
        //Antigua forma de tener una instancia de la base de datos
        //room = Room.databaseBuilder(this, TaskDatabase::class.java, "task_table").build()
        room = TaskDatabase.getDatabase(this)

        //getData()
        initUI()
        CoroutineScope(Dispatchers.IO).launch {
            room.taskDao().getAllTask().collect { tasks ->
                // Actualizar la UI con la lista de tareas
                runOnUiThread {
                    printData(tasks)
                }
            }
        }
    }

    private fun updateSelected(day: Int, month: Int, year: Int) {
        Toast.makeText(this, year.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun initUI() {

        binding.btnAddNewTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }


//            val dialog = Dialog(this)
//            dialog.setContentView(R.layout.item_add_task)
//            val buttonAddTask: Button = dialog.findViewById(R.id.btnAddTask)
//            val titleTarea = dialog.findViewById<EditText>(R.id.etTitleTask)
//            val descriptionTarea = dialog.findViewById<EditText>(R.id.etDescriptionTask)
//            buttonAddTask.setOnClickListener {
//                if (titleTarea.text.isNotEmpty() && descriptionTarea.text.isNotEmpty()){
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val newTask = Task(
//                            titleTask = titleTarea.text.toString(),
//                            description = descriptionTarea.text.toString(),
//                            type = "Prueba"
//                        )
//                        room.taskDao().insert(newTask)
//                    }
//                    dialog.hide()
//                }
//            }
//            dialog.setCanceledOnTouchOutside(false)
//            dialog.show()

    }

//    private fun getData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val data = room.taskDao().getAllTask()
//            runOnUiThread {
//                printData(data)
//            }
//        }
//    }
    private fun verTareaCompleta(id: Int){
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra(BANDERA, true)  // Boolean
        intent.putExtra(ID, id)        // Long ID (o usa putExtra("id", 123) para un Int)
        startActivity(intent)
    }
    private fun marcarCompletado(view: ImageView){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_ask_complete)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnComplete = dialog.findViewById<Button>(R.id.btnComplete)
        btnComplete.setOnClickListener {
            view.setColorFilter(ContextCompat.getColor(this, R.color.complete_task));
            dialog.hide()
        }
        btnCancel.setOnClickListener {
            dialog.hide()
        }
        dialog.show()
    }
    private fun deleteTask(id: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar tarea")
        builder.setMessage("Estas seguro de que quieres eliminar la tarea?")
        builder.setNegativeButton("Cancelar"){dialog, _ ->
            // Cierra el diálogo cuando se hace clic en "Cancelar"
            dialog.dismiss()
        }
        builder.setPositiveButton("Si") { dialog, _ ->
            // Cierra el diálogo cuando se hace clic en "OK"
            CoroutineScope(Dispatchers.IO).launch {
                room.taskDao().deleteTaskById(id)
            }
            dialog.dismiss()
        }

        // Crea y muestra el diálogo
        val dialog = builder.create()
        dialog.show()
    }
    private fun printData(data: List<Task>) {
        taskAdapter = ListTaskAdapter(data, {marcarCompletado(it)},{deleteTask(it)}, {verTareaCompleta(it)})
        binding.rvListaDeActividades.layoutManager = LinearLayoutManager(this)
        binding.rvListaDeActividades.adapter = taskAdapter
    }
}