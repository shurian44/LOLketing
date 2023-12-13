package com.ezen.lolketing.view.main.my_page.cache

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityCacheChargingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CacheChargingActivity :
    StatusViewModelActivity<ActivityCacheChargingBinding, CacheChargingViewModel>(R.layout.activity_cache_charging) {

    override val viewModel: CacheChargingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    } // onCreate()

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@CacheChargingActivity
        vm = viewModel
        layoutTop.btnBack.setOnClickListener { finish() }
    }

    /** 각 금액 버튼을 클릭 ***/
    fun plusCacheClick(amount: Long) {
        viewModel.updateChargeAmount(amount)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCacheDetailInfo()
    }
}