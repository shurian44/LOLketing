package com.ezen.lolketing.view.main.ticket.detail

import android.os.Bundle
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityReserveDetailBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.toCommaWon
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReserveDetailActivity : BaseActivity<ActivityReserveDetailBinding>(R.layout.activity_reserve_detail) {

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

        val hallAFragment = HallFragment("A") { seat ->
            txtSelectSeat.text = seat
        }

        viewPager.adapter = SeatAdapter(this@ReserveDetailActivity).also {
            it.addFragment(hallAFragment)
        }

        chPersonnelOne.setOnChangeListener {
            if(it) {
                chPersonnelTwo.isChecked = false
                hallAFragment.setSeatCount(1)
                txtCurrentAmount.text = 11000L.toCommaWon()
            }
        }

        chPersonnelTwo.setOnChangeListener {
            if(it) {
                chPersonnelOne.isChecked = false
                hallAFragment.setSeatCount(2)
                txtCurrentAmount.text = 22000L.toCommaWon()
            }
        }
    }
}