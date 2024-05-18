package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogPaymentBinding
import com.ezen.lolketing.util.backgroundTransparent
import com.ezen.lolketing.util.dialogResize
import com.ezen.lolketing.util.priceFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PaymentDialog(
    myCash: Int,
    totalPrice: Long,
    private val onClick: () -> Unit
) : DialogFragment() {

    private val binding: DialogPaymentBinding by lazy { DialogPaymentBinding.inflate(layoutInflater) }
    val myCashFormat: StateFlow<String> = MutableStateFlow(myCash.priceFormat())
    val totalPriceFormat: StateFlow<String> = MutableStateFlow(totalPrice.priceFormat())

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
        onClick()
        dismiss()
    }

    fun onCancelClick() {
        dismiss()
    }

}