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
import com.ezen.lolketing.view.main.ticket.TicketingActivity
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
        viewModel.getUserInfo()
    }

    private fun eventHandler(event: PaymentViewModel.Event) {
        when(event) {
            is PaymentViewModel.Event.SeatSuccess -> {
                viewModel.generateQRCode(getImageName())
            }
            is PaymentViewModel.Event.Failure -> {
                binding.btnChargingCache.isEnabled = true
                binding.progressBar.isVisible = false
                toast(getString(R.string.error_unexpected))
            }
            is PaymentViewModel.Event.QrCodeSuccess -> {
                viewModel.setPurchase(
                    amount = documentedIdList?.size ?: 1,
                    downloadUrl = event.downloadUrl,
                    team = binding.txtGameTitle.text.toString(),
                    price = binding.txtPrice.text.toString().removePriceFormat(),
                    information = "${binding.txtTime.text}, ${binding.txtSeat.text}"
                )
            }
            is PaymentViewModel.Event.PurchaseSuccess -> {
                viewModel.myCacheDeduction(binding.txtPrice.text.toString().removePriceFormat())
            }
            is PaymentViewModel.Event.MyCache -> {
                setMyCache(event.cache)
            }
            is PaymentViewModel.Event.PaymentSuccess -> {
                binding.progressBar.isVisible = false
                toast(getString(R.string.payment_success))

                startActivity(
                    createIntent(TicketingActivity::class.java).also {

                    }
                )
                setResult(Activity.RESULT_OK)
                finish()
            }
            is PaymentViewModel.Event.UserInfoFailure -> {
                toast(getString(R.string.error_user_info_search))
            }
        }
    }

    private fun getImageName() = "${binding.txtTime.text}_${binding.txtGameTitle.text}_${binding.txtSeat.text.toString().replace(", ", "_")}"

    private fun setMyCache(cache: Long) = with(binding) {
        txtMyCache.text = cache.priceFormat()
        btnChargingCache.isVisible = (txtPrice.text.toString() > txtMyCache.text.toString())
    }

    fun onChargingCacheClick(view: View) {
        launcher.launch(createIntent(CacheChargingActivity::class.java))
    }

    fun onTicketInfoClick(view: View) {
        startActivity(ReserveActivity::class.java)
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

        binding.progressBar.isVisible = true
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
            viewModel.getUserInfo()
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