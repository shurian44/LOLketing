package com.ezen.lolketing.view.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.EventSliderAdapter
import com.ezen.lolketing.databinding.ActivityMainBinding
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.MainViewModel
import com.ezen.lolketing.view.dialog.TeamSelectDialog
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.login.join.JoinDetailActivity
import com.ezen.lolketing.view.main.board.BoardListActivity
import com.ezen.lolketing.view.main.chatting.ChattingListActivity
import com.ezen.lolketing.view.main.event.EventListActivity
import com.ezen.lolketing.view.main.guide.LoLGuideActivity
import com.ezen.lolketing.view.main.league_info.LeagueInfoActivity
import com.ezen.lolketing.view.main.manager.ManagerActivity
import com.ezen.lolketing.view.main.my_page.MyPageActivity
import com.ezen.lolketing.view.main.news.NewsActivity
import com.ezen.lolketing.view.main.shop.ShopActivity
import com.ezen.lolketing.view.main.ticket.ReserveListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : BaseViewModelActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()
    var auth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> handleEvent(event) }
        }

        initViews()

    }

    private fun initViews() {
        viewModel.getUserInfo()
        viewModel.getEventBannerList()

        binding.btnBoard.setOnClickListener {
            TeamSelectDialog{ team ->
                startActivity(
                    Intent(this, BoardListActivity::class.java).also { intent ->
                        intent.putExtra("team", team)
                    }
                )
            }.show(supportFragmentManager, "")
        }
    }

    private fun handleEvent(event : MainViewModel.MainEvent) {
        when(event) {
            is MainViewModel.MainEvent.CheckDetailJoin -> {
                checkDetailJoin(event)
            }
            is MainViewModel.MainEvent.EventBannerList -> {
                eventSlide(event.list)
            }
            is MainViewModel.MainEvent.Error -> {
                toast(event.msg)
            }
        }
    }

    // 상세 회원가입 여부 조회
    private fun checkDetailJoin(event : MainViewModel.MainEvent.CheckDetailJoin){
        if (event.isNotJoinComplete){
            startActivity(JoinDetailActivity::class.java)
        }

        if (event.isMaster) {
            binding.btnManager.isVisible = true
        }
    }

    // 이벤트 베너 등록
    private fun eventSlide(list : List<Int>) = with(binding.imgAd) {
        // AUTO Slider
        setSliderAdapter(EventSliderAdapter(list))
        setIndicatorAnimation(IndicatorAnimations.WORM)
        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        indicatorSelectedColor = Color.WHITE
        indicatorUnselectedColor = Color.GRAY
        scrollTimeInSec = 4
        startAutoCycle()
    }

    // 각 버튼별 페이지 이동
    fun moveActivity(view: View) {
        when(view.id){
            R.id.btn_event -> startActivity(EventListActivity::class.java)
            R.id.btn_myPage -> startActivity(MyPageActivity::class.java)
            R.id.btn_info -> startActivity(LeagueInfoActivity::class.java)
            R.id.btn_reserve -> startActivity(ReserveListActivity::class.java)
            R.id.btn_shop -> startActivity(ShopActivity::class.java)
            R.id.btn_guid -> startActivity(LoLGuideActivity::class.java)
            R.id.btn_news -> startActivity(NewsActivity::class.java)
            R.id.btn_chatting -> startActivity(ChattingListActivity::class.java)
        }
    }

    // 로그아웃 버튼 클릭
    fun logout(view: View) {
        auth.signOut()

        startActivity(
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        )
        finish()
    }

    // 관리자 페이지 버튼 클릭
    fun managerPage(view: View) {
        startActivity(ManagerActivity::class.java)
    }
}
