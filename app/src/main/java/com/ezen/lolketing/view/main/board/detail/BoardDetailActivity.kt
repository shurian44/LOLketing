package com.ezen.lolketing.view.main.board.detail

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.CommentAdapter
import com.ezen.lolketing.databinding.ActivityBoardDetailBinding
import com.ezen.lolketing.model.CommentItem
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.dialog.BoardMenuPopup
import com.ezen.lolketing.view.dialog.DialogReport
import com.ezen.lolketing.view.main.board.comment.CommentActivity
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardDetailActivity : BaseViewModelActivity<ActivityBoardDetailBinding, BoardDetailViewModel>(R.layout.activity_board_detail) {

    override val viewModel : BoardDetailViewModel by viewModels()

    private var team: String? = null
    private var documentID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

        // 뷰 세팅
        initViews()

    } // onCreate

    private fun eventHandler(event: BoardDetailViewModel.Event) {
        dismissDialog()

        when(event) {
            is BoardDetailViewModel.Event.Loading -> {
                showDialog()
            }
            is BoardDetailViewModel.Event.BoardInfoSuccess -> {
                binding.board = event.board
                setLikeView(event.board.like?.get(viewModel.email) ?: false)
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

    /** 각종 뷰 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@BoardDetailActivity
        documentID = intent.getStringExtra(Constants.DOCUMENT_ID) ?: kotlin.run {
            toast(getString(R.string.error_unexpected))
            finish()
            return@with
        }
        team = intent.getStringExtra(Constants.TEAM)
        title = team

        layoutTop.btnBack.setOnClickListener {  onBackClick(it) }

        viewModel.getBoard(documentID)

    } // setViews

    /** ...(더보기) 버튼 클릭 **/
    fun onMoreClick(view: View) {
        createPopupMenu().showPopup(view)
    }

    /** 더보기 팝업 생성 **/
    private fun createPopupMenu() =
        BoardMenuPopup(layoutInflater).also {
            when(viewModel.email) {
                // 본인의 글인 경우 수정/삭제
                binding.board?.email -> {
                    it.createPopup(BoardMenuPopup.MY_BOARD)
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
                        }
                    )
                }
                // 타인의 글인 경우 댓글/신고
                else -> {
                    it.createPopup(BoardMenuPopup.ANOTHER_PERSON_BOARD)
                    it.setListener(
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
            }
        }

    /** 좋아요 클릭 **/
    fun onLikeClick(view: View) {
        viewModel.updateLike(documentID)
        binding.viewHeart.isEnabled = false
        binding.txtLikeCount.isEnabled = false
    }

    /** 댓글 클릭 **/
    fun onCommentClick(view: View) {
        launcher.launch(
            createIntent(CommentActivity::class.java).also {
                it.putExtra(Constants.DOCUMENT_ID, documentID)
                it.putExtra(Constants.TEAM, team)
                it.putExtra(CommentActivity.CATEGORY, binding.board?.category)
                it.putExtra(CommentActivity.TITLE, binding.board?.title)
            }
        )
    }

    /** 좋아요 뷰 셋팅 **/
    private fun setLikeView(isLike: Boolean) = with(binding) {
        binding.viewHeart.isEnabled = true
        binding.txtLikeCount.isEnabled = true

        if (isLike) {
            viewHeart.setBackgroundResource(R.drawable.ic_heart_fill)
        } else {
            viewHeart.setBackgroundResource(R.drawable.ic_heart)
        }
    }

    /** 어뎁터 설정 **/
    private fun setAdapter(list: List<CommentItem>) = with(binding) {
        commentCount = list.size
        val adapter = CommentAdapter(
            layoutInflater = layoutInflater,
            myEmail = viewModel.email
        ).also {
            val maxLength = if (list.size > 5) 5 else list.size
            it.addList(list.subList(0, maxLength))

            it.setDeleteListener { commentDocumentId ->
                viewModel.deleteComment(documentID, commentDocumentId)
            }

            it.setReportListener { commentDocumentId, reportList ->
                val resultList = reportList?.toMutableList() ?: mutableListOf()

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
