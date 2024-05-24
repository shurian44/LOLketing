package com.ezen.lolketing.view.main.ticket.detail

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityReserveDetailBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnCreated
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.dialog.CashChargingDialog
import com.ezen.lolketing.view.main.ticket.ReserveActivity
import com.ezen.lolketing.view.main.ticket.info.MyTicketInfoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ReserveDetailActivity :
    StatusViewModelActivity<ActivityReserveDetailBinding, ReserveDetailViewModel>(R.layout.activity_reserve_detail) {

    override val viewModel: ReserveDetailViewModel by viewModels()

    val adapter by lazy { HallAdapter(viewModel::updateSeatState) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        repeatOnCreated {
            viewModel.uiStatus.collectLatest {
                when (it) {
                    is Reservation.Init -> {}
                    is Reservation.CashCharging -> {
                        showCashChargingDialog(it.myCash)
                        viewModel.updateInit()
                    }

                    is Reservation.Success -> {
                        goToTicketInfo(it.ids)
                    }
                }
            }
        }

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@ReserveDetailActivity
        vm = viewModel

        txtSelectHall.text = "A홀"
        layoutTop.btnBack.setOnClickListener { finish() }
        chPersonnelOne.setOnClick { viewModel.changePerson(1) }
        chPersonnelTwo.setOnClick { viewModel.changePerson(2) }
    }

    /** 티켓 안내 클릭 **/
    fun goToReserve() {
        startActivity(ReserveActivity::class.java)
    }

    private fun goToTicketInfo(ids: String) {
        startActivity(
            createIntent(MyTicketInfoActivity::class.java).also {
                it.putExtra(Constants.ID, ids)
            }
        )
        finish()
    }

    private fun showCashChargingDialog(myCash: String) {
        CashChargingDialog(
            myCash = myCash,
            onOkClick = viewModel::cashCharging
        ).show(supportFragmentManager, "")
    }

    override fun onResume() {
        super.onResume()

        intent
            ?.getIntExtra(Constants.ID, 0)
            ?.let(viewModel::fetchReservedSeat)
            ?: run {
                toast(R.string.error_unexpected)
                finish()
            }
    }

}