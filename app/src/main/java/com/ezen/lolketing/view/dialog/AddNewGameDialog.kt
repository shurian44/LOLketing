package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogAddNewGameBinding
import com.ezen.lolketing.util.backgroundTransparent

class AddNewGameDialog(
    val addGameClickListener : (String, String) -> Unit
) : DialogFragment() {

    private val binding : DialogAddNewGameBinding by lazy { DialogAddNewGameBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.backgroundTransparent()
        binding.dialog = this

        // todo 피커 추가 예정

    }

    fun onCancel(view: View) {
        dismiss()
    }

    fun addGameClick(view: View) {
        addGameClickListener(binding.editDate.text.toString(), binding.editTime.text.toString())
        dismiss()
    }

}