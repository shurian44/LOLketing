package com.ezen.lolketing.view.login.find

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityFindIdPwBinding
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FindIdPwActivity : BaseViewModelActivity<ActivityFindIdPwBinding, FindIdPwViewModel>(R.layout.activity_find_id_pw) {

    override val viewModel: FindIdPwViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }
        binding.activity = this

    }

    private fun eventHandler(event: FindIdPwViewModel.Event) {
        when(event) {
            is FindIdPwViewModel.Event.FindSuccess -> {
                viewModel.sendPasswordResetEmail(binding.editId.text.toString())
            }
            is FindIdPwViewModel.Event.FindFailure -> {
                toast(getString(R.string.error_not_found_id))
            }
            is FindIdPwViewModel.Event.SendEmailSuccess -> {
                toast(getString(R.string.guide_resent_password))
            }
            is FindIdPwViewModel.Event.SendEmailFailure -> {
                toast(getString(R.string.error_unexpected))
            }
        }
    }

    fun findId(view: View) {
        viewModel.getFindUser(binding.editId.text.toString())
    }

}