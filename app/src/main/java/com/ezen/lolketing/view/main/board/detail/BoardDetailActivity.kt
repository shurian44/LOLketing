package com.ezen.lolketing.view.main.board.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.CommentAdapter
import com.ezen.lolketing.databinding.ActivityBoardDetailBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnCreated
import com.ezen.lolketing.view.main.board.menu.BoardMenuAdapter
import com.ezen.lolketing.view.main.board.menu.BoardMenuBalloonFactory
import com.ezen.lolketing.view.main.board.menu.BoardMenuItem
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import com.skydoves.balloon.balloon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BoardDetailActivity :
    StatusViewModelActivity<ActivityBoardDetailBinding, BoardDetailViewModel>(R.layout.activity_board_detail) {

    override val viewModel: BoardDetailViewModel by viewModels()
    val adapter by lazy {
        CommentAdapter()
    }
    private val balloon by balloon<BoardMenuBalloonFactory>()
    private val balloonAdapter = BoardMenuAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    } // onCreate

    private fun initViews() = with(binding) {
        activity = this@BoardDetailActivity
        vm = viewModel
        layoutTop.btnBack.setOnClickListener { finish() }

        editComment.setRegisterListener {
            viewModel.insertComment(it)
            editComment.setText("")
        }

        val listRecyclerView =
            balloon.getContentView().findViewById<RecyclerView>(R.id.boardMenuRecyclerView)
        listRecyclerView.adapter = balloonAdapter

        repeatOnCreated {
            viewModel.info.collectLatest {
                setBalloon(it.isAuthor)
            }
        }

    }

    private fun setBalloon(isAuthor: Boolean) {
        if (isAuthor) {
            balloonAdapter.submitList(
                listOf(
                    BoardMenuItem("수정하기", ::goToModify),
                    BoardMenuItem("삭제하기", viewModel::deleteBoard)
                )
            )
        } else {
            balloonAdapter.submitList(
                listOf(
                    BoardMenuItem("신고하기") {}
                )
            )
        }
    }

    fun updateBoardLike() {
        viewModel.updateBoardLike()
    }

    fun showBalloon(view: View) {
        balloon.showAlignBottom(view)
    }

    private fun goToModify() {
        viewModel.getBoardId()?.let { boardId ->
            startActivity(
                createIntent(BoardWriteActivity::class.java).also {
                    it.putExtras(
                        bundleOf(Constants.BoardId to boardId)
                    )
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchBordInfo()
    }
} // class
