package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogWithdrawlBinding
import com.ezen.lolketing.util.backgroundTransparent
import com.ezen.lolketing.util.dialogResize
import com.ezen.lolketing.util.toast

class WithdrawalDialog(
    private val onOkClick: () -> Unit
) : DialogFragment() {

    private val binding: DialogWithdrawlBinding by lazy {
        DialogWithdrawlBinding.inflate(
            layoutInflater
        )
    }

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

    override fun onResume() {
        super.onResume()
        requireContext().dialogResize(dialog)
    }

    fun onOkClick() {
        if (binding.editWithdrawal.text.toString() == "회원 탈퇴") {
            onOkClick.invoke()
            dismiss()
        } else {
            toast("회원 탈퇴를 정확히 입력해주세요")
        }
    }

}