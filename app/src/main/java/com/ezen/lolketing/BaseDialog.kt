package com.ezen.lolketing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.util.backgroundTransparent

open class BaseDialog<B : ViewDataBinding>(
    @LayoutRes private val layoutId : Int
) : DialogFragment() {

    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)

        with(binding) {
            lifecycleOwner = this@BaseDialog
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그 배경 투명화
        dialog?.backgroundTransparent()
    }

}