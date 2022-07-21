package com.ezen.lolketing.view.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.EventSliderAdapter
import com.ezen.lolketing.databinding.ActivityMainBinding
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.dialog.TeamSelectDialog
import com.ezen.lolketing.view.login.join.JoinDetailActivity
import com.ezen.lolketing.view.main.board.BoardListActivity
import com.ezen.lolketing.view.main.chatting.ChattingListActivity
import com.ezen.lolketing.view.main.event.EventListActivity
import com.ezen.lolketing.view.main.guide.GuideActivity
import com.ezen.lolketing.view.main.my_page.MyPageActivity
import com.ezen.lolketing.view.main.news.NewsActivity
import com.ezen.lolketing.view.main.shop.ShopActivity
import com.ezen.lolketing.view.main.ticket.list.ReserveListActivity
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseViewModelActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> handleEvent(event) }
        }

        initViews()

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() {
        viewModel.getUserInfo()
        viewModel.getEventBannerList()
        binding.activity = this
    }

    /** 이벤트 핸들러 **/
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

    /** 상세 회원가입 여부 조회 **/
    private fun checkDetailJoin(event : MainViewModel.MainEvent.CheckDetailJoin){
        if (event.isNotJoinComplete){
            startActivity(JoinDetailActivity::class.java)
        }
    }

    /** 이벤트 베너 등록 **/
    private fun eventSlide(list : List<Int>) = with(binding.viewPagerAd) {
        // AUTO Slider
        setSliderAdapter(EventSliderAdapter(list))
        setIndicatorAnimation(IndicatorAnimations.WORM)
        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        scrollTimeInSec = 4
        startAutoCycle()
    }

    /** 각 버튼별 페이지 이동 **/
    fun moveActivity(view: View) = with(binding) {
        when(view.id){
            btnEvent.id, txtEvent.id -> startActivity(EventListActivity::class.java)
            btnMyPage.id, txtMyPage.id -> startActivity(MyPageActivity::class.java)
            btnInfo.id, txtInfo.id -> startActivity(com.ezen.lolketing.view.main.league_info.LeagueInfoActivity::class.java)
            btnReserve.id, txtReserve.id -> startActivity(ReserveListActivity::class.java)
            btnShop.id, txtShop.id -> startActivity(ShopActivity::class.java)
            btnGuid.id, txtGuid.id -> startActivity(GuideActivity::class.java)
            btnNews.id, txtNews.id -> startActivity(NewsActivity::class.java)
            btnChatting.id, txtChatting.id -> startActivity(ChattingListActivity::class.java)
            btnBoard.id, txtBoard.id -> {
                TeamSelectDialog{ team ->
                    startActivity(
                        createIntent(BoardListActivity::class.java).also { intent ->
                            intent.putExtra(Constants.TEAM, team)
                        }
                    )
                }.show(supportFragmentManager, "")
            }
        }
    }
}
