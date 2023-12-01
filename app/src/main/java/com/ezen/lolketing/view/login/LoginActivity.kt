package com.ezen.lolketing.view.login

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityLoginBinding
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnCreated
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.login.find.FindPasswordActivity
import com.ezen.lolketing.view.login.join.JoinActivity
import com.ezen.lolketing.view.login.join.JoinDetailActivity
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

        repeatOnCreated {
            viewModel.userInfo.collectLatest { it?.let { user -> goToPage(user) } }
        }

    } // onCreate()

    /** 메인 페이지로 이동 **/
    private fun goToPage(user: Users) {
        if (user.nickname == null) {
            launcher.launch(createIntent(JoinDetailActivity::class.java))
        } else {
            startActivity(MainActivity::class.java)
            finish()
        }
    }

    fun goToFindPassword() {
        startActivity(FindPasswordActivity::class.java)
    }

    fun goToJoin() {
        startActivity(JoinActivity::class.java)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                startActivity(MainActivity::class.java)
                finish()
            } else {
                toast(getString(R.string.guide_register_nickname))
                viewModel.logout()
            }
        }
}