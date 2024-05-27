package com.ezen.lolketing.view.main.ticket.payment

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityPaymentBinding
import com.ezen.lolketing.model.TicketTemp
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.getParcelableExtraCompat
import com.ezen.lolketing.util.showErrorMessageAndFinish
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.main.my_page.cache.CacheChargingActivity
import com.ezen.lolketing.view.main.ticket.ReserveActivity
import com.ezen.lolketing.view.main.ticket.info.MyTicketInfoActivity
import dagger.hilt.android.AndroidEntryPoint

// 제거 예정
@AndroidEntryPoint
class PaymentActivity :
    StatusViewModelActivity<ActivityPaymentBinding, PaymentViewModel>(R.layout.activity_payment) {

    override val viewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        activity = this@PaymentActivity
        vm = viewModel

        intent
            ?.getParcelableExtraCompat<TicketTemp>(INFO)
            ?.let { viewModel.setInfo(it) }
            ?: showErrorMessageAndFinish()

        layoutTop.btnBack.setOnClickListener { finish() }
    }

    /** 캐시 충전 클릭 **/
    fun goToCacheCharging() {
        startActivity(CacheChargingActivity::class.java)
    }

    /** 티켓 안내 클릭 **/
    fun goToReserve() {
        startActivity(ReserveActivity::class.java)
    }

    override fun statusFinish() {
//        startActivity(
//            createIntent(MyTicketInfoActivity::class.java).also {
//                it.putExtra(MyTicketInfoActivity.DOCUMENT_ID, viewModel.purchaseId.value)
//            }
//        )
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUserCacheInfo()
    }

    companion object {
        const val INFO = "info"
    }

}