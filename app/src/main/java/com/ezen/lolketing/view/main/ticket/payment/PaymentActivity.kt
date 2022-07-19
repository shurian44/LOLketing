package com.ezen.lolketing.view.main.ticket.payment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityPaymentBinding
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.main.my_page.cache.CacheChargingActivity
import com.ezen.lolketing.view.main.ticket.ReserveActivity
import com.ezen.lolketing.view.main.ticket.info.MyTicketInfoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : BaseViewModelActivity<ActivityPaymentBinding, PaymentViewModel>(R.layout.activity_payment) {

    override val viewModel: PaymentViewModel by viewModels()
    private var documentedIdList : List<String>? = null
    private var purchaseId: String = ""

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
        viewModel.getUserCache()
    }

    private fun eventHandler(event: PaymentViewModel.Event) {
        when(event) {
            is PaymentViewModel.Event.Loading -> {
                showDialog()
            }
            // 내 캐시 조회 성공
            is PaymentViewModel.Event.MyCache -> {
                dismissDialog()
                setMyCache(event.cache)
            }
            // 좌석 정보 업데이트 성공 -> qr 코드 생성하기
            is PaymentViewModel.Event.SeatSuccess -> {
                viewModel.generateQRCode(getImageName())
            }
            // qr 코드 저장 성공 -> 구매 내역 추가하기
            is PaymentViewModel.Event.QrCodeSuccess -> {
                viewModel.setPurchase(
                    amount = documentedIdList?.size ?: 1,
                    downloadUrl = event.downloadUrl,
                    team = binding.txtGameTitle.text.toString(),
                    price = binding.txtPrice.text.toString().removePriceFormat(),
                    information = "${binding.txtTime.text}, ${binding.txtSeat.text}",
                    documentList = documentedIdList ?: listOf()
                )
            }
            // 구매 내역 저장 성공 -> 캐시 소모 업데이트하기
            is PaymentViewModel.Event.PurchaseSuccess -> {
                purchaseId = event.documentId
                viewModel.myCacheDeduction(binding.txtPrice.text.toString().removePriceFormat())
            }
            // 결제하기 성공
            is PaymentViewModel.Event.PaymentSuccess -> {
                dismissDialog()
                toast(getString(R.string.payment_success))

                startActivity(
                    createIntent(MyTicketInfoActivity::class.java).also {
                        it.putExtra(MyTicketInfoActivity.DOCUMENT_ID, purchaseId)
                    }
                )
                setResult(Activity.RESULT_OK)
                finish()
            }
            // 실패
            is PaymentViewModel.Event.Failure -> {
                dismissDialog()
                binding.btnChargingCache.isEnabled = true
                toast(getString(R.string.error_unexpected))
            }
            // 유저 정보 조회 실패
            is PaymentViewModel.Event.UserInfoFailure -> {
                dismissDialog()
                toast(getString(R.string.error_user_info_search))
            }
        }
    }

    /** qr 코드 저장 이름 **/
    private fun getImageName() = "${binding.txtTime.text}_${binding.txtGameTitle.text}_${binding.txtSeat.text.toString().replace(", ", "_")}"

    /** 캐시 정보 셋팅 **/
    private fun setMyCache(cache: Long) = with(binding) {
        txtMyCache.text = cache.priceFormat()
        // 티켓 값보다 캐시가 적으면 캐시 충전 버튼 활성화
        btnChargingCache.isVisible = (txtPrice.text.toString().removePriceFormat() > txtMyCache.text.toString().removePriceFormat())
    }

    /** 캐시 충전 클릭 **/
    fun onChargingCacheClick(view: View) {
        launcher.launch(createIntent(CacheChargingActivity::class.java))
    }

    /** 티켓 안내 클릭 **/
    fun onTicketInfoClick(view: View) {
        startActivity(ReserveActivity::class.java)
    }

    /** 결제하기 클릭 **/
    fun onPaymentClick(view: View) {
        if (binding.txtPrice.text.toString().removePriceFormat() > binding.txtMyCache.text.toString().removePriceFormat()) {
            toast(getString(R.string.out_of_cache))
            return
        }

        if (documentedIdList.isNullOrEmpty()) {
            toast(getString(R.string.error_unexpected))
            Log.e("PaymentActivity", "document id is null")
            return
        }

        view.isEnabled = false
        documentedIdList?.let {
            viewModel.updateSeat(
                firstDocumentId = binding.txtTime.text.toString(),
                secondDocumentIdList = it,
            )
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getUserCache()
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