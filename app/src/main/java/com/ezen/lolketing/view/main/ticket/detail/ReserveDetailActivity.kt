package com.ezen.lolketing.view.main.ticket.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityReserveDetailBinding
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.main.ticket.payment.PaymentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReserveDetailActivity : BaseActivity<ActivityReserveDetailBinding>(R.layout.activity_reserve_detail) {

    private lateinit var hallFragment: HallFragment
    private var documentIdList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    } // onCreate()

    private fun initViews() = with(binding) {
        activity = this@ReserveDetailActivity
        title = getString(R.string.ticket_reserve)
        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        val time = intent?.getStringExtra(Constants.TIME)
        val team = intent?.getStringExtra(Constants.TEAM)

        if (time == null || team == null) {
            toast(getString(R.string.error_unexpected))
            finish()
            return@with
        }

        txtTime.text = time
        txtGameTitle.text = team
        txtCurrentAmount.text = 11000L.toCommaWon()

        txtSelectHall.text = "A홀"
        setFragment()

        chPersonnelOne.setOnChangeListener {
            if(it) {
                chPersonnelTwo.isChecked = false
                hallFragment.setSeatCount(1)
                txtCurrentAmount.text = 11000L.toCommaWon()
            }
        }

        chPersonnelTwo.setOnChangeListener {
            if(it) {
                chPersonnelOne.isChecked = false
                hallFragment.setSeatCount(2)
                txtCurrentAmount.text = 22000L.toCommaWon()
            }
        }
    }

    private fun setFragment() = with(binding) {
        hallFragment = HallFragment(txtSelectHall.text.toString(), txtTime.text.toString()) { seat, documentIdList ->
            txtSelectSeat.text = seat
            this@ReserveDetailActivity.documentIdList = documentIdList
        }

        supportFragmentManager.beginTransaction().also {
            it.replace(fragmentContainer.id, hallFragment, "")
            it.commit()
        }
    }

    fun onReserveClick(view: View) = with(binding) {
        if (txtSelectSeat.text == getString(R.string.guide_select_seat)){
            toast(getString(R.string.guide_select_seat))
            return@with
        }

        if (chPersonnelTwo.isChecked && documentIdList.size != 2) {
            toast(getString(R.string.guide_select_seat))
            return@with
        }

        startActivity(
            createIntent(PaymentActivity::class.java).also {
//                날짜, 게임 타이틀, 좌석, 금액
                it.putExtra(PaymentActivity.TIME, txtTime.text.toString())
                it.putExtra(PaymentActivity.GAME_TITLE, txtGameTitle.text.toString())
                it.putExtra(PaymentActivity.SEAT, txtSelectSeat.text.toString())
                it.putExtra(PaymentActivity.AMOUNT, txtCurrentAmount.text.toString())
                it.putExtra(PaymentActivity.DOCUMENT_ID_LIST, documentIdList)
            }
        )
    }

    fun onTicketInfoClick(view: View) {
        startActivity(PaymentActivity::class.java)
    }

}