package com.ezen.lolketing.view.main.board.detail

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.CommentAdapter
import com.ezen.lolketing.databinding.ActivityBoardDetailBinding
import com.ezen.lolketing.model.Board.Comment
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.dialog.BoardMenuPopup
import com.ezen.lolketing.view.dialog.DialogReport
import com.ezen.lolketing.view.main.board.comment.CommentActivity
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BoardDetailActivity : BaseViewModelActivity<ActivityBoardDetailBinding, BoardDetailViewModel>(R.layout.activity_board_detail) {

    override val viewModel : BoardDetailViewModel by viewModels()
    @Inject lateinit var pref: SharedPreferences

    var team: String? = null
    private var documentID: String = ""
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

        // 뷰 세팅
        initViews()

    } // onCreate

    private fun eventHandler(event: BoardDetailViewModel.Event) {
        when(event) {
            is BoardDetailViewModel.Event.BoardInfoSuccess -> {
                binding.board = event.board
                setLikeView(event.board.like?.get(email) ?: false)
                viewModel.updateViews(documentID)
                viewModel.getUserGrade()
                viewModel.getCommentsList(documentID)
            }
            is BoardDetailViewModel.Event.UserGrade -> {
                setGradeImageView(binding.imgIcon, event.grade)
            }
            is BoardDetailViewModel.Event.CommentsListSuccess -> {
                setAdapter(event.comments)
            }
            is BoardDetailViewModel.Event.LikeUpdateSuccess -> {
                setLikeView(event.like)
                binding.board = event.board
            }
            is BoardDetailViewModel.Event.LikeUpdateFailure -> {
            }
            is BoardDetailViewModel.Event.Failure -> {
                toast(getString(R.string.error_unexpected))
            }
            is BoardDetailViewModel.Event.DeleteSuccess -> {
                toast(getString(R.string.delete_complete))
                finish()
            }
            BoardDetailViewModel.Event.ReportSuccess -> {
                toast(getString(R.string.report_received))
                finish()
            }
            BoardDetailViewModel.Event.CommentDeleteSuccess -> {
                toast(getString(R.string.delete_complete))
                viewModel.getCommentsList(documentID)
            }
            BoardDetailViewModel.Event.CommentReportSuccess -> {
                toast(getString(R.string.report_received))
            }
        }
    }

    // 뷰 메소드 캡슐화
    private fun initViews() = with(binding) {
        activity = this@BoardDetailActivity
        team = intent.getStringExtra(Constants.TEAM)
        title = team
        documentID = intent.getStringExtra(Constants.DOCUMENT_ID) ?: kotlin.run {
            toast(getString(R.string.error_unexpected))
            return@with
        }

        layoutTop.btnBack.setOnClickListener {
            onBackClick(it)
        }

        viewModel.getBoard(documentID)

        email = pref.getString(Constants.ID, null) ?: ""

        viewMore.setOnClickListener {
            createPopupMenu().showPopup(it)
        }

    } // setViews

    private fun createPopupMenu() =
        BoardMenuPopup(layoutInflater).also {
            if (email == binding.board?.email) {
                it.createPopup(BoardMenuPopup.MY_BOARD)
            } else {
                it.createPopup(BoardMenuPopup.ANOTHER_PERSON_BOARD)
            }

            it.setListener(
                modifyListener = {
                    modifyLauncher.launch(
                        createIntent(BoardWriteActivity::class.java).also { intent ->
                            intent.putExtra(Constants.TEAM, team)
                            intent.putExtra(Constants.DOCUMENT_ID, documentID)
                        }
                    )
                },
                deleteListener = {
                    viewModel.deleteBoard(documentID)
                },
                commentListener = {
                    onCommentClick(binding.viewComment)
                },
                reportListener = {
                    val list = mutableListOf<String>()
                    binding.board?.reportList?.let { reportList ->
                        list.addAll(reportList)
                    }

                    DialogReport { select ->
                        list.add(select)

                        viewModel.updateReportList(
                            documentId = documentID,
                            reportList = list
                        )
                    }.show(supportFragmentManager, "")
                }
            )
        }

    fun onLikeClick(view: View) {
        viewModel.updateLike(documentID)
        binding.viewHeart.isEnabled = false
        binding.txtLikeCount.isEnabled = false
    }

    fun onCommentClick(view: View) {
        launcher.launch(
            createIntent(CommentActivity::class.java).also {
                it.putExtra(Constants.DOCUMENT_ID, documentID)
                it.putExtra(CommentActivity.CATEGORY, binding.board?.category)
                it.putExtra(CommentActivity.TITLE, binding.board?.title)
            }
        )
    }

    private fun setLikeView(isLike: Boolean) = with(binding) {
        binding.viewHeart.isEnabled = true
        binding.txtLikeCount.isEnabled = true

        if (isLike) {
            viewHeart.setBackgroundResource(R.drawable.ic_heart_fill)
        } else {
            viewHeart.setBackgroundResource(R.drawable.ic_heart)
        }
    }

    private fun setAdapter(list: List<Comment>) = with(binding) {
        commentCount = list.size
        val adapter = CommentAdapter(
            layoutInflater = layoutInflater,
            myEmail = email
        ).also {
            val maxLength = if (list.size > 5) 5 else list.size
            it.addList(list.subList(0, maxLength))

            it.setDeleteListener { commentDocumentId ->
                viewModel.deleteComment(documentID, commentDocumentId)
            }

            it.setReportListener { commentDocumentId, reportList ->
                val resultList = mutableListOf<String>()
                reportList?.let {
                    resultList.addAll(reportList)
                }

                DialogReport { select ->
                    resultList.add(select)

                    viewModel.updateCommentReport(
                        boardDocumentId = documentID,
                        commentDocumentId = commentDocumentId,
                        reportList = resultList
                    )

                }.show(supportFragmentManager, "")
            }
        }

        recyclerView.adapter = adapter
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.getCommentsList(documentID)
        }
    }

    private val modifyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.getBoard(documentID)
        }
    }

} // class
