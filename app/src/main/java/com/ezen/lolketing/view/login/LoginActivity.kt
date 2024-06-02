package com.ezen.lolketing.view.login

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityLoginBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.find.FindPasswordActivity
import com.ezen.lolketing.view.login.join.JoinActivity
import com.ezen.lolketing.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity :
    StatusViewModelActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    override val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel
    } // onCreate()

    fun goToFindPassword() {
        // 3.0.1 버전에서는 개발에서 제외
//        startActivity(FindPasswordActivity::class.java)
    }

    fun goToJoin() {
        startActivity(
            createIntent(JoinActivity::class.java).also {
                it.putExtra(Constants.JoinType, Constants.Email)
            }
        )
    }

    override fun statusFinish() {
        startActivity(MainActivity::class.java)
        super.statusFinish()
    }
}