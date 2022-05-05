package com.ezen.lolketing

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseViewModelActivity<B : ViewDataBinding, VM : ViewModel>(
    @LayoutRes private val layoutId : Int
) : AppCompatActivity() {

    protected lateinit var binding: B
    abstract val viewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)

        with(binding) {
            lifecycleOwner = this@BaseViewModelActivity
            setVariable(BR._all, viewModel)
        }


    }

}