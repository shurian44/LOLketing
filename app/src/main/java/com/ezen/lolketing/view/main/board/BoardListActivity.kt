package com.ezen.lolketing.view.main.board

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityBoardListBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.Team
import com.ezen.lolketing.util.addOnScrollListener
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.dialog.MenuPopup
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import com.ezen.lolketing.view.main.board.my_board.MyBoardActivity
import com.ezen.lolketing.view.main.board.search.SearchActivity
import com.ezen.lolketing.view.main.board.team.TeamActivity
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardListActivity :
    StatusViewModelActivity<ActivityBoardListBinding, BoardListViewModel>(R.layout.activity_board_list) {

    override val viewModel: BoardListViewModel by viewModels()

    val adapter = BoardListAdapter(
        onclickListener = { documentId ->
            startActivity(
                createIntent(BoardDetailActivity::class.java).also {
                    it.putExtra(Constants.TEAM, viewModel.team.value)
                    it.putExtra(Constants.DOCUMENT_ID, documentId)
                }
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchBoardList()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@BoardListActivity
        vm = viewModel

        viewModel.setTeam(intent?.getStringExtra(Constants.TEAM) ?: Team.T1.name)

        layoutTop.layoutTop.setBackgroundResource(android.R.color.transparent)
        layoutTop.btnBack.setOnClickListener { finish() }
        layoutTop.btnMenu.setOnClickListener { showMenuPopup(it) }

        // 스크롤에따라 타이틀 영역 색상 변경
        recyclerView.addOnScrollListener {
            layoutTop.layoutTop.setBackgroundResource(
                if (recyclerView.computeVerticalScrollOffset() > 200) {
                    R.color.black
                } else {
                    android.R.color.transparent
                }
            )
        }
    }

    private fun showMenuPopup(view: View) {
        MenuPopup(layoutInflater).also { popup ->
            popup.createPopup()
            popup.setListener(
                teamListener = {
                    startActivity(
                        createIntent(TeamActivity::class.java).also { intent ->
                            intent.putExtra(Constants.TEAM, viewModel.team.value)
                        }
                    )
                },
                myBoardListener = {
                    startActivity(
                        createIntent(MyBoardActivity::class.java).also { intent ->
                            intent.putExtra(Constants.TEAM, viewModel.team.value)
                        }
                    )
                }
            )
        }.showPopup(view)
    }

    /** 글쓰기 버튼 클릭 **/
    fun writeBoard() {
        startActivity(
            createIntent(BoardWriteActivity::class.java).also {
                it.putExtra(Constants.TEAM, viewModel.team.value)
            }
        )
    }

    /** 검색 페이지 이동 **/
    fun searchBoard() {
        startActivity(
            createIntent(SearchActivity::class.java).also {
                it.putExtra(Constants.TEAM, viewModel.team.value)
            }
        )
    }

}