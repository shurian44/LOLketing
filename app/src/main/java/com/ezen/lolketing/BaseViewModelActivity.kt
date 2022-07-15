package com.ezen.lolketing

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ezen.lolketing.view.dialog.LoadingDialog

abstract class BaseViewModelActivity<B : ViewDataBinding, VM : ViewModel>(
    @LayoutRes private val layoutId : Int
) : AppCompatActivity() {

    protected lateinit var binding: B
    abstract val viewModel : VM
    protected val dialog = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)

        with(binding) {
            lifecycleOwner = this@BaseViewModelActivity
            setVariable(BR._all, viewModel)
        }

    }

    open fun logout(view: View){}

    open fun moveHome(view: View){}

    open fun onBackClick(view: View) {
        finish()
    }

    protected fun showDialog() {
        dialog.show(supportFragmentManager, null)
    }

    protected fun dismissDialog(){
        dialog.dismissDialog()
    }

}