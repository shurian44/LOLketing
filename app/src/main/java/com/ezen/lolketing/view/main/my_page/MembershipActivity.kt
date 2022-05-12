package com.ezen.lolketing.view.main.my_page

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityMembershipBinding
import com.ezen.lolketing.util.htmlFormat
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MembershipActivity : BaseActivity<ActivityMembershipBinding>(R.layout.activity_membership) {

    @Inject lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        conditionSilver.text = getString(R.string.condition_silver).htmlFormat()
        conditionGold.text = getString(R.string.condition_gold).htmlFormat()
        conditionPlatinum.text = getString(R.string.condition_platinum).htmlFormat()
        benefitBronze.text = getString(R.string.benefit_bronze).htmlFormat()
        benefitSilver.text = getString(R.string.benefit_silver).htmlFormat()
        benefitGold.text = getString(R.string.benefit_gold).htmlFormat()
        benefitPlatinum.text = getString(R.string.benefit_platinum).htmlFormat()
    }


    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }
}