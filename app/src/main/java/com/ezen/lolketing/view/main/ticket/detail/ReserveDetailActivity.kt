package com.ezen.lolketing.view.main.ticket.detail

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityReserveDetailBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.showErrorMessageAndFinish
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.main.ticket.ReserveActivity
import com.ezen.lolketing.view.main.ticket.payment.PaymentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReserveDetailActivity :
    StatusViewModelActivity<ActivityReserveDetailBinding, ReserveDetailViewModel>(R.layout.activity_reserve_detail) {

    override val viewModel: ReserveDetailViewModel by viewModels()

    val adapter by lazy { HallAdapter(viewModel::updateSeatState) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@ReserveDetailActivity
        vm = viewModel

        intent
            ?.getStringExtra(Constants.TIME)
            ?.let { viewModel.setReserveTime(it) }
            ?: { showErrorMessageAndFinish() }

        intent
            ?.getStringExtra(Constants.TEAM)
            ?.let { team = it }

        txtSelectHall.text = "A홀"
        layoutTop.btnBack.setOnClickListener { finish() }
        chPersonnelOne.setOnClick { viewModel.chanePerson(true) }
        chPersonnelTwo.setOnClick { viewModel.chanePerson(false) }
    }

    /** 예매하기 버튼 클릭 **/
    fun goTaPayment() = with(binding) {
        val info = viewModel.getTicketInfo(txtGameTitle.text.toString())
            .onFailure { toast(it.message ?: getString(R.string.error_unexpected)) }
            .getOrNull() ?: return@with

        launcher.launch(
            createIntent(PaymentActivity::class.java).also {
                it.putExtra(PaymentActivity.INFO, info)
            }
        )
    }

    /** 티켓 안내 클릭 **/
    fun goToReserve() {
        startActivity(ReserveActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchReservedSeat()
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

}