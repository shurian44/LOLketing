package com.ezen.lolketing.view.main.ticket.info

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityMyTicketInfoBinding
import com.ezen.lolketing.model.TicketInfo
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MyTicketInfoActivity : BaseViewModelActivity<ActivityMyTicketInfoBinding, MyTicketInfoViewModel>(R.layout.activity_my_ticket_info) {

    override val viewModel: MyTicketInfoViewModel by viewModels()
    private lateinit var documentId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

        intent?.getStringExtra(DOCUMENT_ID)?.let {
            documentId = it
            viewModel.getTicketInfo(it)
        } ?: kotlin.run {
            toast(getString(R.string.error_unexpected))
            finish()
        }
    } // onCreate()

    private fun eventHandler(event: MyTicketInfoViewModel.Event) {
        when(event) {
            is MyTicketInfoViewModel.Event.Success -> {
                initViews(event.info)
            }
            is MyTicketInfoViewModel.Event.Failure -> {
                toast(getString(R.string.error_unexpected))
                finish()
            }
        }
    }

    private fun initViews(info: TicketInfo) = with(binding) {

        activity = this@MyTicketInfoActivity

        val list = info.information.split(", ")

        if (list.isEmpty() || list.size < 2 || info.image.isEmpty()) {
            toast(getString(R.string.error_unexpected))
            finish()
            return@with
        }

        Glide
            .with(this@MyTicketInfoActivity)
            .load(info.image)
            .into(imgQrCode)

        txtGameTitle.text = info.gameTitle
        txtTime.text = list[0]
        if (list.size == 2) {
            txtSeat.text = list[1]
        } else {
            val seat = "${list[1]}, ${list[2]}"
            txtSeat.text = seat
        }

        val isRefundPossible = viewModel.getRefund(
            time = txtTime.text.toString(),
            amount = txtSeat.text.toString().split(", ").size
        )

        btnRefund.apply {
            isEnabled = isRefundPossible
            text = if (isRefundPossible) getString(R.string.refund) else getString(R.string.refund_impossible)
        }

    }

    override fun moveHome(view: View) {
        super.moveHome(view)

    }

    fun refundTicket(view: View)  {
        viewModel.setRefundTicket(
            purchaseDocumentId = documentId,
            time = binding.txtTime.text.toString(),
            seat = binding.txtSeat.text.toString()
        )
    }

    companion object {
        const val DOCUMENT_ID = "document_id"
    }

}