package com.ezen.lolketing.view.main.board.detail

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.exception.BoardException
import com.ezen.lolketing.model.BoardDetailInfo
import com.ezen.lolketing.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val repository: BoardRepository
) : StatusViewModel() {

    private val _info = MutableStateFlow(BoardDetailInfo.create())
    val info: StateFlow<BoardDetailInfo> = _info

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    val isLike: StateFlow<Boolean> = _info.map {
        it.getIsLike(_email.value)
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private var _documentId = ""
    val documentId
        get() = _documentId

    init {
        setEmail()
    }

    private fun setEmail() = runCatching {
        _email.value = repository.fetchEmail()
    }.onFailure {
        updateMessage(it.message ?: "유저 정보가 없습니다.")
        updateFinish(true)
    }

    fun setDocumentId(documentId: String) {
        this._documentId = documentId
    }

    /** 게시글 조회 **/
    fun fetchBordInfo() {
        repository
            .fetchBoardInfo(_documentId)
            .setLoadingState()
            .onEach { _info.value = it }
            .catch {
                updateMessage(it.message ?: "조회 중 오류가 발생하였습니다.")
                if (it is BoardException.SearchError) {
                    updateFinish(true)
                }
            }
            .launchIn(viewModelScope)
    }

    /** 좋아요 업데이트 **/
    fun updateLike() {
        repository
            .updateLikes(
                documentId = _documentId,
                info = _info.value,
                email = _email.value
            )
            .setLoadingState()
            .onEach { fetchBordInfo() }
            .catch { updateMessage(it.message ?: "업데이트 실패") }
            .launchIn(viewModelScope)
    }

    /** 게시글 삭제 **/
    fun deleteBoard() {
        repository
            .deleteBoard(documentId)
            .setLoadingState()
            .onEach {
                updateMessage(it)
                updateFinish(true)
            }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다") }
            .launchIn(viewModelScope)
    }

    /** 게시글 신고하기 **/
    fun updateReportList(report: String) {
        val newReportList = _info.value.reportList.toMutableList().also {
            it.add(report)
        }

        repository
            .updateBoardReport(
                documentId = _documentId,
                reportList = newReportList,
            )
            .setLoadingState()
            .onEach {
                updateMessage(it)
                _info.update { info -> info.copy(reportList = newReportList) }
            }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

    /** 댓글 신고하기 **/
    fun updateCommentReport(
        commentDocumentId: String,
        reportList: List<String>,
        report: String
    ) = viewModelScope.launch {
        repository
            .updateCommentReport(
                boardDocumentId = documentId,
                commentDocumentId = commentDocumentId,
                reportList = reportList.toMutableList().also {
                    it.add(report)
                },
            )
    }

    /** 댓글 삭제 **/
    fun deleteComment(
        commentDocumentId: String
    ) = viewModelScope.launch {
        repository
            .deleteComment(
                boardDocumentId = documentId,
                commentDocumentId = commentDocumentId,
                count = _info.value.commentList.size - 1
            )
            .setLoadingState()
            .onEach { fetchCommentList() }
            .launchIn(viewModelScope)
    }

    /** 댓글 조회 **/
    private fun fetchCommentList() {
        repository
            .fetchComments(documentId = documentId)
            .setLoadingState()
            .onEach {
                _info.update { item ->
                    item.copy(
                        commentList = it,
                        commentCounts = it.size.toLong()
                    )
                }
            }
            .catch { }
            .launchIn(viewModelScope)
    }

}