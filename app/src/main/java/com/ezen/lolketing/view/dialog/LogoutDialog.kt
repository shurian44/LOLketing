package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogLogoutBinding
import com.ezen.lolketing.util.backgroundTransparent
import com.ezen.lolketing.util.dialogResize

class LogoutDialog(
    private val onOkClick: () -> Unit
) : DialogFragment() {

    private val binding: DialogLogoutBinding by lazy { DialogLogoutBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그 배경 투명화
        dialog?.backgroundTransparent()
        binding
        binding.dialog = this

    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogResize(dialog)
    }

    fun onOkClick() {
        onOkClick.invoke()
        dismiss()
    }

}