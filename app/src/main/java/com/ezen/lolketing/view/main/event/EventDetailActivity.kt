package com.ezen.lolketing.view.main.event

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityEventDetailBinding
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.htmlFormat
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class EventDetailActivity : BaseViewModelActivity<ActivityEventDetailBinding, EventDetailViewModel>(R.layout.activity_event_detail) {
    override val viewModel: EventDetailViewModel by viewModels()
    @Inject lateinit var auth : FirebaseAuth
    private var coupon = Coupon()
    private var documentID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

    } // onCreate()

    private fun initViews() = with(binding) {
        val page = intent.getIntExtra("page", 1)
        // 신규 회원가입 페이지의 경우
        if(page == 1) {
            setNewUserPage()
        }

        // 티켓 구매 이벤트 안내 페이지 일 경우
        if(page == 2){
            imgMain.setImageResource(R.drawable.banner2)
            eventTxt1.text = getString(R.string.event_ticket_purchase).htmlFormat()
            btnCoupon.isVisible = false
        }
    }

    private fun setNewUserPage() = with(binding){
        imgMain.setImageResource(R.drawable.banner1)
        viewModel.getUserNickname()
        viewModel.getCouponList()
    } // setNewUserPage()

    private fun eventHandler(event: EventDetailViewModel.Event) {
        when(event) {
            is EventDetailViewModel.Event.UserNickname -> {
                binding.eventTxt1.text = getString(R.string.event_new_user, event.nickname).htmlFormat()
            }
            is EventDetailViewModel.Event.CouponList -> {
                Log.e("+++++", "${event.list}")
                if (event.list.isEmpty()){
                    toast("쿠폰 조회를 실패하였습니다.")
                    finish()
                    return
                }
                coupon = event.list[0]
            }
            is EventDetailViewModel.Event.Error -> {
                toast(event.msg)
            }
        }
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    companion object {
        const val PAGE = "page"
    }

}