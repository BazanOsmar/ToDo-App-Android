package com.example.todoapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.databinding.ActivityAddTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var room: TaskDatabase
    private var bandera: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obteniendo la instancia de la base de datos
        room = TaskDatabase.getDatabase(this)

        if (intent.hasExtra(TodoListActivity.BANDERA) && intent.hasExtra(TodoListActivity.ID)) {
            val isValid = intent.getBooleanExtra(TodoListActivity.BANDERA, false)
            val id = intent.getIntExtra(TodoListActivity.ID, 0)
            if (isValid){
                CoroutineScope(Dispatchers.IO).launch {
                    val data: Task = room.taskDao().getById(id)
                    runOnUiThread {
                        bandera = !bandera
                        mostrarDetallesTarea(data)
                    }
                }
            }
        }

        listeners()

        //Boton Back
        onBackPressedDispatcher.addCallback (this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Crear un AlertDialog
                backActivity()
            }
        })
    }

    private fun mostrarDetallesTarea(data: Task) {
        binding.ContainerIconos.visibility = View.GONE




        binding.etTileTask.setText(data.titleTask)
        binding.etTileTask.isEnabled = false

        binding.etContent.setText(data.description)
        binding.etContent.isEnabled = false

        binding.tvDate.text = data.date
        binding.tvDate.isClickable = false
    }

    private fun backActivity() {
        if (binding.etTileTask.text.isNotEmpty() && binding.etContent.text.isNotEmpty() && bandera){
            showDialog()
        }else{
            finish()
        }
    }

    private fun listeners() {
        binding.btnBack.setOnClickListener {
            backActivity()
        }
        binding.btnAdd.setOnClickListener {
            saveData()
        }
        binding.btnCalendar.setOnClickListener {
            val datePicKer = DatePicker{day, month, year -> updateSelected(day,month,year) }
            datePicKer.show(supportFragmentManager, "Fecha")
        }
        binding.tvDate.setOnClickListener {
            dialogQuitDate()
        }
    }

    private fun dialogQuitDate() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Quieres quitar la fecha?")
        builder.setNegativeButton("Cancelar"){dialog, _ ->
            // Cierra el diálogo cuando se hace clic en "Cancelar"
            dialog.dismiss()
        }
        builder.setPositiveButton("Si") { dialog, _ ->
            // Cierra el diálogo cuando se hace clic en "OK"
            binding.tvDate.text = ""
        }

        // Crea y muestra el diálogo
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateSelected(day: Int, month: Int, year: Int) {
        binding.tvDate.text = "$day / $month / $year"
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_go_out_activity)
        val btnSave: Button = dialog.findViewById(R.id.btnSave)
        val btnNoSave: Button = dialog.findViewById(R.id.btnDontSave)
        btnSave.setOnClickListener {
            saveData()
            dialog.dismiss()
        }
        btnNoSave.setOnClickListener {
            finish()
        }
        dialog.show()
    }
    private fun saveData(){
        if (binding.etTileTask.text.isNotEmpty() && binding.etContent.text.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                val newTask = Task(
                    titleTask = binding.etTileTask.text.toString(),
                    description = binding.etContent.text.toString(),
                    type = "Personal",
                    date = binding.tvDate.text.takeIf { it.isNotEmpty() }?.toString() ?: "Sin Fecha"
                )
                room.taskDao().insert(newTask)
            }
            finish()
        }else{
            Toast.makeText(this, "Llena los campos", Toast.LENGTH_SHORT).show()
        }
    }

}