package com.ezen.lolketing

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.dialog.LoadingDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

abstract class StatusViewModelActivity<B : ViewDataBinding, VM : StatusViewModel>(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity() {
    protected lateinit var binding: B
    abstract val viewModel: VM
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)

        with(binding) {
            lifecycleOwner = this@StatusViewModelActivity
            setVariable(BR._all, viewModel)
        }

        val status = viewModel.status.value
        repeatOnStarted {
            status.message.collectLatest {
                if (it.isEmpty()) return@collectLatest
                runCatching {
                    toast(it)
                    delay(500)
                    status.updateMessage("")
                }
            }
        }

        repeatOnStarted {
            status.isLoading.collectLatest { if (it) showDialog() else dismissDialog() }
        }

        repeatOnStarted {
            status.isFinish.collectLatest { if (it) statusFinish() }
        }
    }

    open fun statusFinish() {
        finish()
    }

    private fun showDialog() = runCatching {
        loadingDialog = LoadingDialog.newInstance().also {
            it.show(supportFragmentManager, "loading")
        }
    }

    private fun dismissDialog() {
        loadingDialog?.dismissDialog()
        loadingDialog = null
    }
}