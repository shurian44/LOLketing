package com.ezen.lolketing.view.main.board.comment

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.repository.BoardRepository
import com.ezen.lolketing.view.main.board.detail.BoardDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: BoardRepository
) : BaseViewModel<CommentViewModel.Event>(){

    fun getCommentList(
        documentId: String
    ) = viewModelScope.launch {
        repository
            .getCommentsList(
                documentId = documentId,
                successListener = {
                    event(Event.CommentListSuccess(it))
                },
                failureListener = {
                    event(Event.Failure)
                }
            )
    }

    fun addComment(
        documentId: String,
        comment: String
    ) = viewModelScope.launch {
        repository
            .addComment(
                documentId = documentId,
                comment = comment,
                successListener = {
                    event(Event.AddCommentSuccess)
                },
                failureListener = {
                    event(Event.AddCommentFailure)
                }
            )
    }

    fun updateCommentCount(
        documentId: String,
        count: Int
    ) = viewModelScope.launch {
        repository
            .updateBoardCommentCount(
                documentId = documentId,
                count = count
            )
    }

    fun updateCommentReport(
        boardDocumentId: String,
        commentDocumentId: String,
        reportList: List<String>
    ) = viewModelScope.launch {
        repository
            .updateCommentReport(
                boardDocumentId = boardDocumentId,
                commentDocumentId = commentDocumentId,
                reportList =  reportList,
                successListener = {
                    event(Event.CommentReportSuccess)
                }
            )
    }

    fun deleteComment(
        boardDocumentId: String,
        commentDocumentId: String
    ) = viewModelScope.launch {
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
        data class CommentListSuccess(
            val list : List<Board.Comment>
        ) : Event()
        object AddCommentSuccess : Event()
        object AddCommentFailure : Event()
        object CommentReportSuccess : Event()
        object CommentDeleteSuccess : Event()
        object Failure : Event()
    }

}