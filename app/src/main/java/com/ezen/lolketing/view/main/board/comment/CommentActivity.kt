package com.ezen.lolketing.view.main.board.comment

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.CommentAdapter
import com.ezen.lolketing.databinding.ActivityCommentBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.repeatOnCreated
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


// 제거 예정
@AndroidEntryPoint
class CommentActivity :
    StatusViewModelActivity<ActivityCommentBinding, CommentViewModel>(R.layout.activity_comment) {

    override val viewModel: CommentViewModel by viewModels()
    val adapter by lazy {
        CommentAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        repeatOnCreated {
            viewModel.isSuccess.collectLatest {
                if (it) binding.editComment.setText("")
            }
        }

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@CommentActivity
        vm = viewModel
        intent?.getStringExtra(Constants.DOCUMENT_ID)?.let {
            viewModel.setDocumentId(it)
        } ?: run {
            toast(getString(R.string.error_unexpected))
            finish()
        }

        intent.getStringExtra(CATEGORY)?.let { category = it }
        intent.getStringExtra(TITLE)?.let { boardTitle = it }

        layoutTop.btnBack.setOnClickListener { finish() }

        editComment.setRegisterListener {
            if (it.isEmpty()) {
                toast(getString(R.string.input_contents))
                return@setRegisterListener
            }
            viewModel.addComment(it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCommentList()
    }

    companion object {
        const val CATEGORY = "category"
        const val TITLE = "title"
    }

}