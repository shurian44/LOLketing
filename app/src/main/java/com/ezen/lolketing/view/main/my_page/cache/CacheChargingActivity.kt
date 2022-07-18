package com.ezen.lolketing.view.main.my_page.cache

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityCacheChargingBinding
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.util.removePriceFormat
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CacheChargingActivity : BaseViewModelActivity<ActivityCacheChargingBinding, CacheChargingViewModel>(R.layout.activity_cache_charging) {

    override val viewModel: CacheChargingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

    } // onCreate()

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@CacheChargingActivity
        title = getString(R.string.charging_cache)
        layoutTop.btnBack.setOnClickListener { finish() }

        viewModel.getUserCache()
    }

    private fun eventHandler(event : CacheChargingViewModel.Event) {
        when(event) {
            is CacheChargingViewModel.Event.RetainedCache -> {
                binding.txtRetainedCache.text = event.cache.priceFormat()
            }
            is CacheChargingViewModel.Event.Success -> {
                toast(getString(R.string.success_charging_cache))
                setResult(Activity.RESULT_OK)
                finish()
            }
            is CacheChargingViewModel.Event.Failure -> {
                toast(getString(R.string.error_unexpected))
                finish()
            }
        }
    }

    /** 각 금액 버튼을 클릭 ***/
    fun plusCacheClick(view: View) {
        val before = binding.txtCache.text.toString().removePriceFormat()

        // 금액에 맞게 충전할 캐시 증가
        val charging = when(view.id){
            binding.btnAddThousand.id -> 10_000
            binding.btnAddTenThousand.id -> 100_000
            binding.btnAddHundredThousand.id -> 1_000_000
            else -> 0
        }

        val after = if (before + charging > 100_000_000) {
            100_000_000
        } else {
            before + charging
        }

        binding.txtCache.text = after.priceFormat()
    }

    /** 충전하기 버튼 클릭 **/
    fun chargingClick(view: View){
        viewModel.chargingCache(binding.txtCache.text.toString().removePriceFormat())
    }
}