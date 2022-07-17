package com.ezen.lolketing.view.main.board.detail

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.model.CommentItem
import com.ezen.lolketing.repository.BoardRepository
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.Grade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val repository: BoardRepository,
    private val pref : SharedPreferences
) : BaseViewModel<BoardDetailViewModel.Event>(){

    private lateinit var board: Board
    val email: String = pref.getString(Constants.ID, null) ?: ""

    /** 게시글 조회 **/
    fun getBoard(
        documentId : String
    ) = viewModelScope.launch {
        repository.getBoardReadInfo(
            documentId = documentId,
            successListener = {
                board = it
                event(Event.BoardInfoSuccess(it))

                updateViews(documentId)
                getUserGrade()
                getCommentsList(documentId)
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    /** 작성자 유저 등급 조회 **/
    private fun getUserGrade() = viewModelScope.launch {
        val email = board.email ?: return@launch
        repository
            .getUserGrade(
                email = email,
                successListener = {
                    event(Event.UserGrade(it))
                },
                failureListener = {
                    event(Event.UserGrade(Grade.BRONZE.gradeCode))
                }
            )
    }

    /** 조회수 업데이트 **/
    private fun updateViews(
        documentId: String
    ) = viewModelScope.launch {
        val userId = pref.getString(Constants.ID, "")
        if (userId == board.email) return@launch

        repository.updateViews(documentId = documentId)
    }

    /** 좋아요 업데이트 **/
    fun updateLike(
        documentId: String,
    ) = viewModelScope.launch {
        event(Event.Loading)
        val userId = pref.getString(Constants.ID, "") ?: return@launch
        val isLike = board.like?.get(userId) ?: false
        board.like?.set(userId, isLike.not())
        board.likeCounts = board.like?.filter { it.value }?.size?.toLong()

        repository
            .updateLikes(
                documentId = documentId,
                board = board,
                successListener = {
                    event(Event.LikeUpdateSuccess(isLike.not(), board))
                },
                failureListener = {
                    error(Event.Failure)
                }
            )
    }

    /** 댓글 리스트 조회 **/
    fun getCommentsList(
        documentId: String
    ) = viewModelScope.launch {
        repository
            .getCommentsList(
                documentId = documentId,
                successListener = {
                    event(Event.CommentsListSuccess(it))
                },
                failureListener = {
                    event(Event.Failure)
                }
            )
    }

    /** 게시글 삭제 **/
    fun deleteBoard(
        documentId: String
    ) = viewModelScope.launch {
        event(Event.Loading)
        repository
            .deleteBoard(
                documentId = documentId,
                successListener = {
                    event(Event.DeleteSuccess)
                },
                failureListener = {
                    event(Event.Failure)
                }
            )
    }

    /** 게시글 신고하기 **/
    fun updateReportList(
        documentId: String,
        reportList: List<String>
    ) = viewModelScope.launch {
        event(Event.Loading)
        repository.updateBoardReport(
            documentId = documentId,
            reportList = reportList,
            successListener = {
                event(Event.ReportSuccess)
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    /** 댓글 신고하기 **/
    fun updateCommentReport(
        boardDocumentId: String,
        commentDocumentId: String,
        reportList: List<String>
    ) = viewModelScope.launch {
        event(Event.Loading)
        repository
            .updateCommentReport(
                boardDocumentId = boardDocumentId,
                commentDocumentId = commentDocumentId,
                reportList = reportList,
                successListener = {
                    event(Event.CommentReportSuccess)
                }
            )
    }

    /** 댓글 삭제 **/
    fun deleteComment(
        boardDocumentId: String,
        commentDocumentId: String
    ) = viewModelScope.launch {
        event(Event.Loading)
        repository
            .deleteComment(
                boardDocumentId = boardDocumentId,
                commentDocumentId = commentDocumentId,
                successListener = {
                    event(Event.CommentDeleteSuccess)
                },
                failureListener = {
                    event(Event.Failure)
                }
            )
    }

    sealed class Event {
        data class BoardInfoSuccess(
            val board: Board
        ) : Event()
        data class UserGrade(
            val grade: String
        ) : Event()
        data class CommentsListSuccess(
            val comments : List<CommentItem>
        ) : Event()
        data class LikeUpdateSuccess(
            val like: Boolean,
            val board: Board
        ) : Event()
        object DeleteSuccess : Event()
        object ReportSuccess : Event()
        object CommentDeleteSuccess : Event()
        object CommentReportSuccess : Event()
        object Failure : Event()
        object Loading : Event()
    }

}