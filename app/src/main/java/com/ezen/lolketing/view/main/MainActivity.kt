package com.ezen.lolketing.view.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityMainBinding
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.dialog.TeamSelectDialog
import com.ezen.lolketing.view.login.join.JoinDetailActivity
import com.ezen.lolketing.view.main.board.BoardListActivity
import com.ezen.lolketing.view.main.chatting.list.ChattingListActivity
import com.ezen.lolketing.view.main.event.EventListActivity
import com.ezen.lolketing.view.main.guide.GuideActivity
import com.ezen.lolketing.view.main.league_info.LeagueInfoActivity
import com.ezen.lolketing.view.main.my_page.MyPageActivity
import com.ezen.lolketing.view.main.news.NewsActivity
import com.ezen.lolketing.view.main.shop.ShopActivity
import com.ezen.lolketing.view.main.ticket.list.ReserveListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : StatusViewModelActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel
        binding.imageSlider.setImageList(
            listOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
        )

        repeatOnStarted {
            viewModel.isNotJoinComplete.collectLatest {
                if (it.not()) {
                    // 상세 회원 가입 여부 조회
                    startActivity(JoinDetailActivity::class.java)
                }
            }
        }
    }

    /** 각 버튼별 페이지 이동 **/
    fun moveActivity(view: View) = with(binding) {
        when(view.id){
            txtEvent.id -> startActivity(EventListActivity::class.java)
            txtMyPage.id -> startActivity(MyPageActivity::class.java)
            txtInfo.id -> startActivity(LeagueInfoActivity::class.java)
            txtReserve.id -> startActivity(ReserveListActivity::class.java)
            txtShop.id -> startActivity(ShopActivity::class.java)
            txtGuid.id -> startActivity(GuideActivity::class.java)
            txtNews.id -> startActivity(NewsActivity::class.java)
            txtChatting.id -> startActivity(ChattingListActivity::class.java)
            txtBoard.id -> { showTeamSelectDialog() }
        }
    }

    private fun showTeamSelectDialog() {
        TeamSelectDialog{ team ->
            startActivity(
                createIntent(BoardListActivity::class.java).also { intent ->
                    intent.putExtra(Constants.TEAM, team)
                }
            )
        }.show(supportFragmentManager, "")
    }
}
