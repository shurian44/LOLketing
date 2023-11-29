package com.ezen.lolketing.view.login

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityLoginBinding
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.find.FindPasswordActivity
import com.ezen.lolketing.view.login.join.JoinActivity
import com.ezen.lolketing.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginActivity :
    StatusViewModelActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    override val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel

        repeatOnStarted {
            viewModel.loginSuccess.collectLatest { if (it) goToMain() }
        }

    } // onCreate()

    /** 메인 페이지로 이동 **/
    private fun goToMain() {
        startActivity(MainActivity::class.java)
        finish()
    }

    fun goToFindPassword() {
        startActivity(FindPasswordActivity::class.java)
    }

    fun goToJoin() {
        startActivity(JoinActivity::class.java)
    }

}