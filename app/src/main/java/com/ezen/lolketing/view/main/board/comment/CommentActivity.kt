package com.ezen.lolketing.view.main.board.comment

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.CommentAdapter
import com.ezen.lolketing.databinding.ActivityCommentBinding
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.dialog.DialogReport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class CommentActivity : BaseViewModelActivity<ActivityCommentBinding, CommentViewModel>(R.layout.activity_comment) {

    override val viewModel: CommentViewModel by viewModels()
    @Inject lateinit var pref : SharedPreferences
    private lateinit var documentId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

        initViews()

    }

    private fun initViews() = with(binding) {
        intent?.getStringExtra(Constants.DOCUMENT_ID)?.let {
            documentId = it
            viewModel.getCommentList(it)
        } ?: kotlin.run {
            toast(getString(R.string.error_unexpected))
        }

        intent.getStringExtra(CATEGORY)?.let { category = it }
        intent.getStringExtra(TITLE)?.let { boardTitle = it }

        editComment.setRegisterListener {
            if (it.isEmpty()) {
                toast(getString(R.string.input_contents))
                return@setRegisterListener
            }

            viewModel.addComment(
                documentId = documentId,
                comment = it
            )
        }

    }

    private fun eventHandler(event : CommentViewModel.Event) {
        when(event) {
            is CommentViewModel.Event.CommentListSuccess -> {
                setAdapter(event.list)
                viewModel.updateCommentCount(documentId = documentId, count = event.list.size)
            }
            is CommentViewModel.Event.Failure -> {
                toast(getString(R.string.error_unexpected))
            }
            CommentViewModel.Event.AddCommentSuccess -> {
                toast(getString(R.string.add_comment_success))
                setResult(RESULT_OK)
                viewModel.getCommentList(documentId)
                binding.editComment.setText("")
            }
            CommentViewModel.Event.AddCommentFailure -> {
                toast(getString(R.string.add_comment_failure))
            }
            CommentViewModel.Event.CommentDeleteSuccess -> {
                viewModel.getCommentList(documentId)
            }
            CommentViewModel.Event.CommentReportSuccess -> {
                toast(getString(R.string.report_received))
            }
        }
    }

    private fun setAdapter(list: List<Board.Comment>) = with(binding) {

        val email = pref.getString(Constants.ID, "") ?: ""

        val adapter = CommentAdapter(
            layoutInflater = layoutInflater,
            myEmail = email
        ).also {
            it.addList(list)

            it.setDeleteListener { commentDocumentId ->
                viewModel.deleteComment(documentId, commentDocumentId)
            }

            it.setReportListener { commentDocumentId, reportList ->
                val resultList = mutableListOf<String>()
                reportList?.let {
                    resultList.addAll(reportList)
                }

                DialogReport { select ->
                    resultList.add(select)

                    viewModel.updateCommentReport(
                        boardDocumentId = documentId,
                        commentDocumentId = commentDocumentId,
                        reportList = resultList
                    )

                }.show(supportFragmentManager, "")
            }
        }

        recyclerView.adapter = adapter
    }

    companion object {
        const val CATEGORY = "category"
        const val TITLE = "title"
    }

}