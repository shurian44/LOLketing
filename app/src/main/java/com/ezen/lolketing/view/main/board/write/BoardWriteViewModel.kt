package com.ezen.lolketing.view.main.board.write

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.util.Constants
import com.ezen.network.model.BoardWriteInfo
import com.ezen.network.model.Team
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
class BoardWriteViewModel @Inject constructor(
    private val repository: BoardRepository,
    savedStateHandle: SavedStateHandle
) : StatusViewModel() {

    private val _item = MutableStateFlow(BoardWriteItem())
    val item: StateFlow<BoardWriteItem> = _item

    init {
        savedStateHandle.get<Int>(Constants.BoardId)?.let(::fetchBoardInfo)
    }

    fun updateTeam(team: Team) {
        _item.update {
            it.copy(
                info = it.info.copy(
                    teamId = team.teamId,
                    teamName = team.teamName
                )
            )
        }
    }

    fun updateImageUri(uri: Uri?) {
        _item.update {
            it.copy(
                info = it.info.copy(image = uri),
                isImageChanged = true
            )
        }
    }

    /** 게시글 수정 시 게시글 정보 조회 **/
    private fun fetchBoardInfo(boardId: Int) {
        repository
            .fetchBoardDetail(boardId)
            .setLoadingState()
            .onEach {
                val info = it.toBoardWriteInfo()
                _item.update { item ->
                    item.copy(
                        info = info,
                        boardId = boardId,
                        isModify = true
                    )
                }
            }
            .catch {
                updateMessage(it.message ?: "정보를 가져오지 못하였습니다.")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    fun onRegister() {
        val item = _item.value
        if (item.isModify) {
            updateBoard(item.info, item.isImageChanged)
        } else {
            insertBoard(item.info)
        }
    }

    /** 게시글 업로드 **/
    private fun insertBoard(info: BoardWriteInfo) {
        repository
            .insertBoard(info)
            .setLoadingState()
            .onEach {
                updateMessage(it)
                updateFinish()
            }
            .catch { updateMessage(it.message ?: "업로드 실패") }
            .launchIn(viewModelScope)
    }

    /** 게시글 수정 **/
    private fun updateBoard(info: BoardWriteInfo, isImageChanged: Boolean) {
        repository
            .updateBoard(info, isImageChanged)
            .setLoadingState()
            .onEach {
                updateMessage(it)
                updateFinish()
            }
            .catch { updateMessage(it.message ?: "수정 실패") }
            .launchIn(viewModelScope)
    }

}

data class BoardWriteItem(
    val info: BoardWriteInfo = BoardWriteInfo.init(),
    val isImageChanged: Boolean = false,
    val isModify: Boolean = false,
    val boardId: Int = 0
)