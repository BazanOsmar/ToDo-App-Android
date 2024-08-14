package com.example.todoapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePicker(val listener:(day: Int, month:Int, year:Int)->Unit):DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth, month, year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val moth = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        val datePick = DatePickerDialog(activity as Context, R.style.datePicker,this, year, moth, day)
        datePick.datePicker.minDate = c.timeInMillis
        return  datePick
    }
}