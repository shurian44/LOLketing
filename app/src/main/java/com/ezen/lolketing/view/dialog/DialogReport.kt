package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseDialog
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.DialogReportBinding
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.toast

class DialogReport(
    private val listener : (String) -> Unit
) : BaseDialog<DialogReportBinding>(R.layout.dialog_report) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialog = this

    }

    fun onCancel(view: View) {
        dismiss()
    }

    fun onOkClick(view: View) {
        when(binding.radioGroup.checkedRadioButtonId) {
            binding.radioPromotion.id -> {
                listener(Code.REPORT_PROMOTION.code)
            }
            binding.radioObscenity.id -> {
                listener(Code.REPORT_OBSCENITY.code)
            }
            binding.radioCopyright.id -> {
                listener(Code.REPORT_COPYRIGHT.code)
            }
            binding.radioEtc.id -> {
                listener(Code.REPORT_ETC.code)
            }
            else -> {
                toast(getString(R.string.report_type))
                return
            }
        }

        dismiss()

    }

}