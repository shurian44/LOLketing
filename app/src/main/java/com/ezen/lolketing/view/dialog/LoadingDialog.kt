package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogLoadingBinding
import com.ezen.lolketing.util.backgroundTransparent

class LoadingDialog() : DialogFragment() {
    private val binding : DialogLoadingBinding by lazy { DialogLoadingBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.backgroundTransparent()
        isCancelable = false

    }

    fun dismissDialog() {
        if (dialog?.isShowing == true) {
            dismiss()
        }
    }

}