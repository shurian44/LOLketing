package com.ezen.lolketing.view.main.board.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.util.Constants
import com.ezen.network.model.BoardDetail
import com.ezen.network.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val repository: BoardRepository,
    savedStateHandle: SavedStateHandle
) : StatusViewModel() {

    private val boardId = savedStateHandle.get<Int>(Constants.BoardId)

    private val _info = MutableStateFlow(BoardDetail.init())
    val info: StateFlow<BoardDetail> = _info

    fun getBoardId() =
        if (boardId == null) {
            updateMessage("게시글 정보가 없습니다.")
            null
        } else boardId


    /** 게시글 조회 **/
    fun fetchBordInfo() {
        if (boardId == null) {
            updateMessage("게시글 정보가 없습니다.")
            updateFinish()
            return
        }

        repository
            .fetchBoardDetail(boardId)
            .setLoadingState()
            .onEach { detail ->
                _info.update { detail }
            }
            .catch {
                updateMessage(it.message ?: "게시글 정보가 없습니다.")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    fun insertComment(contents: String) {
        if (boardId == null) {
            updateMessage("게시글 정보가 없습니다.")
            return
        }

        repository
            .insertComment(
                contents = contents,
                boardId = boardId
            )
            .setLoadingState()
            .onEach { comments ->
                _info.update { it.copy(commentList = comments) }
            }
            .catch { updateMessage(it.message ?: "댓글 작성 실패") }
            .launchIn(viewModelScope)
    }

    fun updateBoardLike() {
        if (boardId == null) {
            updateMessage("게시글 정보가 없습니다.")
            return
        }

        repository
            .updateBoardLike(boardId)
            .onEach { board ->
                _info.update {
                    it.copy(likeCount = board.likeCount, isLike = board.isLike)
                }
            }
            .catch { updateMessage(it.message ?: "좋아요 업데이트 오류") }
            .launchIn(viewModelScope)
    }

}