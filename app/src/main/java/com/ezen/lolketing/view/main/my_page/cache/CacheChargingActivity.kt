package com.ezen.lolketing.view.main.my_page.cache

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityCacheChargingBinding
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class CacheChargingActivity : BaseViewModelActivity<ActivityCacheChargingBinding, CacheChargingViewModel>(R.layout.activity_cache_charging) {

    override val viewModel: CacheChargingViewModel by viewModels()
    @Inject lateinit var auth : FirebaseAuth
    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

        binding.activity = this@CacheChargingActivity

    } // onCreate()

    private fun eventHandler(event : CacheChargingViewModel.Event) {

    }

    // 각 금액 버튼을 클릭
    fun plusCacheClick(view: View) {
        // 금액에 맞게 충전할 캐시 증가
        val charging = when(view.id){
            R.id.btn_cache1 -> 100
            R.id.btn_cache2 -> 1000
            R.id.btn_cache3 -> 10000
            R.id.btn_cache4 -> 100000
            else -> 0
        }

        binding.txtCache.text = "${viewModel.charging(charging).priceFormat()} 원"
    }

    fun chargingClick(view: View) {
        email = auth.currentUser?.email!!
        // 유저 정보 받아오기
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    override fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }
}