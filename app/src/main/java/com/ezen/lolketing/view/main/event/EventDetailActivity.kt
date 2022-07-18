package com.ezen.lolketing.view.main.event

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityEventDetailBinding
import com.ezen.lolketing.util.htmlFormat
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailActivity : BaseViewModelActivity<ActivityEventDetailBinding, EventDetailViewModel>(R.layout.activity_event_detail) {
    override val viewModel: EventDetailViewModel by viewModels()
    private var documentId = ""
    private var isUsed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

    } // onCreate()

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        val page = intent.getIntExtra(PAGE, 1)
        // 신규 회원가입 페이지의 경우
        if(page == 1) {
            setNewUserPage()
        }

        // 티켓 구매 이벤트 안내 페이지 일 경우
        if(page == 2){
            setTicketPurchase()
        }
    }

    /** 신규 가입 이벤트 UI **/
    private fun setNewUserPage() = with(binding){
        activity = this@EventDetailActivity
        title = getString(R.string.event_title_new_user)
        imgMain.setImageResource(R.drawable.banner1)
        viewModel.getUserNickname()
        viewModel.getNewUserCouponInfo()
    } // setNewUserPage()

    /** 티켓 구입 이벤트 UI **/
    private fun setTicketPurchase() = with(binding) {
        title = getString(R.string.event_title_ticket_reserve)
        imgMain.setImageResource(R.drawable.banner2)
        txtEvent.text = getString(R.string.event_ticket_purchase).htmlFormat()
        btnCoupon.isVisible = false
    }

    private fun eventHandler(event: EventDetailViewModel.Event) {
        when(event) {
            is EventDetailViewModel.Event.UserNickname -> {
                binding.txtEvent.text = getString(R.string.event_new_user, event.nickname).htmlFormat()
            }
            is EventDetailViewModel.Event.NewUserCoupon -> {
                isUsed = event.isUsed
                documentId = event.documentId
            }
            is EventDetailViewModel.Event.UpdateSuccess -> {
                isUsed = true
                toast(getString(R.string.new_user_coupon_used))
            }
            is EventDetailViewModel.Event.Error -> {
                toast(event.msg)
            }
        }
    }

    /** 신규 가입 쿠폰 사용 **/
    fun userCoupon(view: View) {
        if (isUsed) {
            toast(getString(R.string.already_used_coupon))
            return
        }

        viewModel.updateCouponUsed(documentId = documentId)
    }

    companion object {
        const val PAGE = "page"
    }

}