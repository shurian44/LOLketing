package com.ezen.lolketing.view.main.ticket.payment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityPaymentBinding
import com.ezen.lolketing.util.fromCommaWon
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PaymentActivity : BaseViewModelActivity<ActivityPaymentBinding, PaymentViewModel>(R.layout.activity_payment) {

    override val viewModel: PaymentViewModel by viewModels()
    private var documentedIdList : List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

    }

    private fun initViews() = with(binding) {
        activity = this@PaymentActivity
        intent?.let {
            txtTime.text = it.getStringExtra(TIME)
            txtGameTitle.text = it.getStringExtra(GAME_TITLE)
            txtSeat.text = it.getStringExtra(SEAT)
            txtPrice.text = it.getStringExtra(AMOUNT)
            documentedIdList = it.getStringArrayListExtra(DOCUMENT_ID_LIST)?.toList()
        }

        layoutTop.btnBack.setOnClickListener { onBackClick(it) }
        title = getString(R.string.ticket_payment)
        // todo 내 캐시 조회, 결제 후 캐시 차감, 캐시 충전 구현 필요
        txtMyCache.text = "15,000원"
    }

    private fun eventHandler(event: PaymentViewModel.Event) {
        when(event) {
            is PaymentViewModel.Event.SeatSuccess -> {
                viewModel.generateQRCode(getImageName())
            }
            is PaymentViewModel.Event.Failure -> {
                toast(getString(R.string.error_unexpected))
            }
            is PaymentViewModel.Event.QrCodeSuccess -> {
                viewModel.setPurchase(
                    amount = documentedIdList?.size ?: 1,
                    downloadUrl = event.downloadUrl,
                    team = binding.txtGameTitle.text.toString(),
                    price = binding.txtPrice.text.toString().fromCommaWon(),
                    information = "${binding.txtTime.text}, ${binding.txtSeat.text}"
                )
            }
            is PaymentViewModel.Event.PurchaseSuccess -> {
                toast("결제완료")
            }
        }
    }

    private fun getImageName() = "${binding.txtTime.text}_${binding.txtGameTitle.text}_${binding.txtSeat.text.toString().replace(", ", "_")}"

    fun onChargingCacheClick(view: View) {
        toast("준비중입니다.")
    }

    fun onTicketInfoClick(view: View) {
        startActivity(PaymentActivity::class.java)
    }

    fun onPaymentClick(view: View) {
        if (binding.txtPrice.text.toString() > binding.txtMyCache.text.toString()) {
            toast(getString(R.string.out_of_cache))
            return
        }

        if (documentedIdList.isNullOrEmpty()) {
            toast(getString(R.string.error_unexpected))
            Log.e("PaymentActivity", "document id is null")
            return
        }

        documentedIdList?.let {
            viewModel.updateSeat(
                firstDocumentId = binding.txtTime.text.toString(),
                secondDocumentIdList = it,
            )
        }
    }

    companion object {
        const val TIME = "time"
        const val GAME_TITLE = "game_title"
        const val SEAT = "seat"
        const val AMOUNT = "amount"
        const val DOCUMENT_ID_LIST = "document_id_list"
    }

}