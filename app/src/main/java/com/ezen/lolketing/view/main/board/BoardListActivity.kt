package com.ezen.lolketing.view.main.board

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityBoardListBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.dialog.TeamSelectDialog
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardListActivity :
    StatusViewModelActivity<ActivityBoardListBinding, BoardListViewModel>(R.layout.activity_board_list) {

    override val viewModel: BoardListViewModel by viewModels()

    val adapter = BoardListAdapter(
        onClickListener = { boardId ->
            startActivity(
                createIntent(BoardDetailActivity::class.java).also {
//                    it.putExtra(Constants.TEAM, viewModel.team.value)
//                    it.putExtra(Constants.DOCUMENT_ID, documentId)
                }
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@BoardListActivity
        vm = viewModel
    }

    fun showTeamSelectDialog() {
        TeamSelectDialog(
            isNeedAll = false,
            onClick = viewModel::updateTeam
        ).show(supportFragmentManager, "")
    }

    /** 글쓰기 버튼 클릭 **/
    fun writeBoard() {
//        startActivity(
//            createIntent(BoardWriteActivity::class.java).also {
//                it.putExtra(Constants.TEAM, viewModel.team.value)
//            }
//        )
    }

}