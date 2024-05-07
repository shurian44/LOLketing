package com.ezen.lolketing.view.main.my_page

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityMyPageBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.dialog.CashChargingDialog
import com.ezen.lolketing.view.dialog.LogoutDialog
import com.ezen.lolketing.view.dialog.WithdrawalDialog
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.login.join.JoinDetailActivity
import com.ezen.lolketing.view.main.shop.history.PurchaseHistoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageActivity :
    StatusViewModelActivity<ActivityMyPageBinding, MyPageViewModel>(R.layout.activity_my_page) {

    override val viewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    fun initViews() = with(binding) {
        vm = viewModel
        activity = this@MyPageActivity
        layoutTop.btnBack.setOnClickListener { finish() }
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
        CashChargingDialog(
            onOkClick = viewModel::updateCashCharging,
            myCash = viewModel.info.value.getFormatCash()
        ).show(supportFragmentManager, null)
    }

    fun logout() {
        LogoutDialog(
            onOkClick = viewModel::logout
        ).show(supportFragmentManager, null)
    }

    fun withdrawal() {
        WithdrawalDialog(
            onOkClick = viewModel::withdrawal
        ).show(supportFragmentManager, null)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUserInfo()
    }

    override fun statusFinish() {
        finishAffinity()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
}