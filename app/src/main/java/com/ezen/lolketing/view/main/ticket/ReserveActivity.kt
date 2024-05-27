package com.ezen.lolketing.view.main.ticket

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityReserveBinding

class ReserveActivity : BaseViewModelActivity<ActivityReserveBinding, ReserveViewModel>(R.layout.activity_reserve) {
    override val viewModel: ReserveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            layoutTop.btnBack.setOnClickListener { onBackClick(it) }
            activity = this@ReserveActivity
            vm = viewModel
        }

    }// onCreate()

    fun onCategoryClick(view: View) {
        when(view.id) {
            R.id.txtReservationInformation -> {
                viewModel.updateStatus(ReserveState.ReservationInformation)
                setUpDownAnimation(
                    view = binding.viewReservationInformation,
                    isUp = viewModel.uiStatus.value.isReservationInformation.not()
                )
            }
            R.id.txtRefundInformation -> {
                viewModel.updateStatus(ReserveState.RefundInformation)
                setUpDownAnimation(
                    view = binding.viewRefundInformation,
                    isUp = viewModel.uiStatus.value.isRefundInformation.not()
                )
            }
            R.id.txtPickUpTicket -> {
                viewModel.updateStatus(ReserveState.PickUpTicket)
                setUpDownAnimation(
                    view = binding.viewPickUpTicket,
                    isUp = viewModel.uiStatus.value.isPickUpTicket.not()
                )
            }
            R.id.txtNotice -> {
                viewModel.updateStatus(ReserveState.Notice)
                setUpDownAnimation(
                    view = binding.viewNotice,
                    isUp = viewModel.uiStatus.value.isNotice.not()
                )
            }
            R.id.txtRestrictions -> {
                viewModel.updateStatus(ReserveState.Restrictions)
                setUpDownAnimation(
                    view = binding.viewRestrictions,
                    isUp = viewModel.uiStatus.value.isRestrictions.not()
                )
            }
        }
    }

    private fun setUpDownAnimation(view: View, isUp: Boolean) {
        val start = if (isUp) 0f else 180f
        val end = if (isUp) 180f else 0f

        ObjectAnimator.ofFloat(view, "rotation", start, end).also {
            it.duration = 300
            it.start()
        }
    }

}