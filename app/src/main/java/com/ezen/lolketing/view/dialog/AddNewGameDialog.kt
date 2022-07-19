package com.ezen.lolketing.view.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogAddNewGameBinding
import com.ezen.lolketing.util.backgroundTransparent
import com.ezen.lolketing.util.getCurrentDateTime
import com.ezen.lolketing.util.timestampToString

class AddNewGameDialog(
    val addGameClickListener : (String, String) -> Unit
) : DialogFragment() {

    private val binding : DialogAddNewGameBinding by lazy { DialogAddNewGameBinding.inflate(layoutInflater) }
    private var year = 2022
    private var month = 1
    private var date = 1
    private var hour = 17

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.backgroundTransparent()
        binding.dialog = this

        val today = getCurrentDateTime().time.timestampToString("yyyy.MM.dd")
        today.split(".").apply {
            year = get(0).toInt()
            month = get(1).toInt()
            date = get(2).toInt()
        }
        setDate()
        setTime()
    }

    fun selectDate(view: View) {
        context?.let {
            DatePickerDialog(
                it,
                { _, year, month, dayOfMonth ->
                    this.year = year
                    this.month = month + 1
                    this.date = dayOfMonth

                    setDate()
                },
                year,
                month-1,
                date
            ).show()
        }
    }

    fun selectTime(view: View) {
        context?.let {
            TimePickerDialog(
                it,
                { _, hour, _ ->
                    this.hour = hour
                    setTime()
                },
                hour,
                0,
                true
            ).show()
        }
    }

    private fun setDate() {
        binding.editDate.setText("$year.${this.month.toString().padStart(2, '0')}.${date.toString().padStart(2, '0')}")
    }

    private fun setTime() {
        binding.editTime.setText("${hour.toString().padStart(2, '0')}:00")
    }

    fun onCancel(view: View) {
        dismiss()
    }

    fun addGameClick(view: View) {
        addGameClickListener(binding.editDate.text.toString(), binding.editTime.text.toString())
        dismiss()
    }

}