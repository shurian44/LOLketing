package com.ezen.lolketing.view.main.ticket.info

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityMyTicketInfoBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.showErrorMessageAndFinish
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.main.ticket.ReserveActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTicketInfoActivity :
    StatusViewModelActivity<ActivityMyTicketInfoBinding, MyTicketInfoViewModel>(R.layout.activity_my_ticket_info) {

    override val viewModel: MyTicketInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    } // onCreate()

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@MyTicketInfoActivity
        vm = viewModel
        layoutTop.btnBack.setOnClickListener { finish() }
        mapView.getMapAsync {
            val marker = Marker()
            marker.position = LatLng(37.5711096, 126.9813897)
            marker.captionText = "LoL PARK"
            marker.map = it

            it.moveCamera(
                CameraUpdate.scrollTo(LatLng(37.5711096, 126.9813897))
            )
        }
    }

    fun goToReservation() {
        startActivity(ReserveActivity::class.java)
    }

    override fun onResume() {
        super.onResume()

        intent?.getStringExtra(Constants.ID)?.let {
            viewModel.fetchTicketInfo(it)
        } ?: showErrorMessageAndFinish()

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
}