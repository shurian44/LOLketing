package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogTeamBinding
import com.ezen.lolketing.util.backgroundTransparent

class TeamSelectDialog(private val listener : (String) -> Unit) : DialogFragment() {

    private val binding : DialogTeamBinding by lazy { DialogTeamBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그 배경 투명화
        dialog?.backgroundTransparent()
        binding.dialog = this

    }


    fun onClick(team : String) {
        listener(team)
        dismiss()
    }

    fun onCancel(view: View) {
        dismiss()
    }

}