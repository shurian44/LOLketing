package com.ezen.lolketing.view.main.board.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.CommentAdapter
import com.ezen.lolketing.databinding.ActivityBoardDetailBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.dialog.BasicPopup
import com.ezen.lolketing.view.dialog.DialogReport
import com.ezen.lolketing.view.dialog.PopupItem
import com.ezen.lolketing.view.main.board.comment.CommentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardDetailActivity :
    StatusViewModelActivity<ActivityBoardDetailBinding, BoardDetailViewModel>(R.layout.activity_board_detail) {

    override val viewModel: BoardDetailViewModel by viewModels()
    val adapter by lazy {
        CommentAdapter(
            layoutInflater = layoutInflater,
            myEmail = viewModel.email.value,
            onDelete = { viewModel.deleteComment(it) },
            onReport = { documentId, list ->
                DialogReport { report ->
                    viewModel
                        .updateCommentReport(
                            commentDocumentId = documentId,
                            reportList = list,
                            report = report
                        )
                }.show(supportFragmentManager, "")
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent
            .getStringExtra(Constants.DOCUMENT_ID)
            ?.let { viewModel.setDocumentId(it) }
            ?: run {
                toast(getString(R.string.error_unexpected))
                finish()
            }

        binding.activity = this
        binding.vm = viewModel
        binding.layoutTop.btnBack.setOnClickListener { finish() }

    } // onCreate

    override fun onResume() {
        super.onResume()

        viewModel.fetchBordInfo()
    }

    /** ...(더보기) 버튼 클릭 **/
    fun onMoreClick(view: View) {
        if (viewModel.info.value.email == viewModel.email.value) {
            myBoardPopup(view)
        } else {
            otherBoardPopup(view)
        }
    }

    /** 내 게시글일 때 팝업 **/
    private fun myBoardPopup(view: View) {
        BasicPopup(
            view = view,
            layoutInflater = layoutInflater,
            list = listOf(
                PopupItem(
                    text = getString(R.string.select_modify),
                    onClick = {}
                ),
                PopupItem(
                    text = getString(R.string.select_delete),
                    onClick = {}
                )
            )
        ).showPopup()
    }

    /** 다른 사람의 게시글일 때 팝업 **/
    private fun otherBoardPopup(view: View) {
        BasicPopup(
            view = view,
            layoutInflater = layoutInflater,
            list = listOf(
                PopupItem(
                    text = getString(R.string.select_comment),
                    onClick = { onCommentClick() }
                ),
                PopupItem(
                    text = getString(R.string.select_report),
                    onClick = {
                        DialogReport {
                            viewModel.updateReportList(it)
                        }.show(supportFragmentManager, "")
                    }
                )
            )
        ).showPopup()
    }

    /** 댓글 클릭 **/
    fun onCommentClick() {
        startActivity(
            createIntent(CommentActivity::class.java).also {
                it.putExtra(Constants.DOCUMENT_ID, viewModel.documentId)
                it.putExtra(CommentActivity.CATEGORY, viewModel.info.value.getCategoryName())
                it.putExtra(CommentActivity.TITLE, viewModel.info.value.title)
            }
        )
    }

} // class
