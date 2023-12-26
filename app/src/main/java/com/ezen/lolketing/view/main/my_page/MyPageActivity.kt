package com.ezen.lolketing.view.main.my_page

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityMyPageBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.login.join.JoinDetailActivity
import com.ezen.lolketing.view.main.my_page.cache.CacheChargingActivity
import com.ezen.lolketing.view.main.shop.history.PurchaseHistoryActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyPageActivity :
    StatusViewModelActivity<ActivityMyPageBinding, MyPageViewModel>(R.layout.activity_my_page) {

    override val viewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.activity = this
        binding.layoutTop.btnBack.setOnClickListener { finish() }

        repeatOnStarted {
            viewModel.goToLogin.collectLatest {
                if (it) {
                    finishAffinity()
                    startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
            }
        }
    }

    fun goToModify() {
        startActivity(
            createIntent(JoinDetailActivity::class.java).also {
                it.putExtra(JoinDetailActivity.MODIFY, true)
            }
        )
    }

    fun goToPurchaseHistory() {
        startActivity(PurchaseHistoryActivity::class.java)
    }

    fun goToCacheCharging() {
        startActivity(CacheChargingActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUserInfo()
    }
}