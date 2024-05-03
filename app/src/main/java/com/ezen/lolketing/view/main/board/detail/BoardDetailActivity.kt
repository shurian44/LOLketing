package com.ezen.lolketing.view.main.board.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.CommentAdapter
import com.ezen.lolketing.databinding.ActivityBoardDetailBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardDetailActivity :
    StatusViewModelActivity<ActivityBoardDetailBinding, BoardDetailViewModel>(R.layout.activity_board_detail) {

    override val viewModel: BoardDetailViewModel by viewModels()
    val adapter by lazy {
        CommentAdapter()
    }

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
    }

    fun updateBoardLike() {
        viewModel.updateBoardLike()
    }

    fun goToModify() {
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
