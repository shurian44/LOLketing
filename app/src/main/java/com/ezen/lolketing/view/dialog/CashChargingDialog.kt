package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogCashChargingBinding
import com.ezen.lolketing.util.backgroundTransparent
import com.ezen.lolketing.util.dialogResize
import com.ezen.lolketing.util.toast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CashChargingDialog(
    private val onOkClick: (Int) -> Unit,
    myCash: String
) : DialogFragment() {

    private val _cash = MutableStateFlow(myCash)
    val cash: StateFlow<String> = _cash

    private val binding: DialogCashChargingBinding by lazy {
        DialogCashChargingBinding.inflate(
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
        runCatching {
            val amount = binding.editAmount.text.toString().toInt()
            if (amount >= 1_000) {
                onOkClick.invoke(amount)
                dismiss()
            } else {
                toast("최소 충전 금액은 1,000원 입니다. 충전 금액을 확인해주세요")
            }
        }
    }

}