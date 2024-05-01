package com.ezen.lolketing.view.main.board.comment

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.CommentItem
import com.ezen.lolketing.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
// 제거 예정
@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: BoardRepository
) : StatusViewModel() {

    private var _documentId = ""
    val documentId
        get() = _documentId

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _list = MutableStateFlow(listOf<CommentItem>())
    val list: StateFlow<List<CommentItem>> = _list

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun setDocumentId(documentId: String) {
        _documentId = documentId
    }

    init {
        setEmail()
    }

    private fun setEmail() = runCatching {
        _email.value = repository.fetchEmail()
    }.onFailure {
        updateMessage(it.message ?: "유저 정보가 없습니다.")
        updateFinish(true)
    }

    /** 댓글 리스트 조회 **/
    fun fetchCommentList() {
        repository
            .fetchComments(documentId)
            .setLoadingState()
            .onEach { _list.value = it }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

    /** 댓글 추가 **/
    fun addComment(comment: String) {
        repository
            .addComment(
                documentId = documentId,
                comment = comment,
                count = _list.value.size + 1
            )
            .setLoadingState()
            .onEach {
                fetchCommentList()
                _isSuccess.value = true
            }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다") }
            .launchIn(viewModelScope)
    }

    /** 댓글 신고 업데이트 **/
    fun updateCommentReport(
        commentDocumentId: String,
        reportList: List<String>,
        report: String
    ) {

        repository
            .updateCommentReport(
                boardDocumentId = documentId,
                commentDocumentId = commentDocumentId,
                reportList = reportList.toMutableList().also {
                    it.add(report)
                },
            )
            .setLoadingState()
            .onEach { updateMessage(it) }
            .catch { updateMessage(it.message ?: "신고 실패") }
            .launchIn(viewModelScope)
    }

    /** 댓글 삭제 **/
    fun deleteComment(commentDocumentId: String) {
        repository
            .deleteComment(
                boardDocumentId = documentId,
                commentDocumentId = commentDocumentId,
                count = _list.value.size - 1
            )
            .setLoadingState()
            .onEach { fetchCommentList() }
            .catch { updateMessage(it.message ?: "삭제 실패") }
            .launchIn(viewModelScope)
    }

}