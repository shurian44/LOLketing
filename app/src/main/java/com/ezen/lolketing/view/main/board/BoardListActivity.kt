package com.ezen.lolketing.view.main.board

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityBoardListBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.addOnScrollListener
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.dialog.TeamSelectDialog
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardListActivity :
    StatusViewModelActivity<ActivityBoardListBinding, BoardListViewModel>(R.layout.activity_board_list) {

    override val viewModel: BoardListViewModel by viewModels()

    val adapter by lazy {
        BoardListAdapter(
            onItemClick = { boardId ->
                startActivity(
                    createIntent(BoardDetailActivity::class.java).also {
                        it.putExtras(
                            bundleOf(Constants.BoardId to boardId)
                        )
                    }
                )
            },
            onLikeClick = viewModel::updateLike,
            onMenuClick = {}
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@BoardListActivity
        vm = viewModel

        recyclerView.addOnScrollListener {
            if (!recyclerView.canScrollVertically(1)) {
                viewModel.fetchBoardList()
            }
        }
    }

    fun showTeamSelectDialog() {
        TeamSelectDialog(
            isNeedAll = true,
            onClick = viewModel::updateTeam
        ).show(supportFragmentManager, "")
    }

    /** 글쓰기 버튼 클릭 **/
    fun writeBoard() {
        startActivity(BoardWriteActivity::class.java)
    }

}