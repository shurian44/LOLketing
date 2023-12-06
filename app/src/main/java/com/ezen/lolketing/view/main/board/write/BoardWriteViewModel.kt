package com.ezen.lolketing.view.main.board.write

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.exception.BoardException
import com.ezen.lolketing.model.BoardWriteInfo
import com.ezen.lolketing.repository.BoardRepository
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
    private val repository: BoardRepository
) : StatusViewModel() {

    private val _info = MutableStateFlow(BoardWriteInfo.create())
    val info: StateFlow<BoardWriteInfo> = _info

    private var isImageChange = false
    private var documentId = ""

    fun setTeam(team: String) {
        _info.update { it.copy(team = team) }
    }

    fun setDocumentId(documentId: String) {
        this.documentId = documentId
        fetchBoardInfo()
    }

    fun updateCategory(category: String) {
        _info.update {
            it.copy(category = category)
        }
    }

    fun updateImageUri(uri: Uri?) {
        _info.update {
            it.copy(image = uri)
        }
        isImageChange = true
    }

    /** 게시글 조회 : 수정 시에만 조회 **/
    private fun fetchBoardInfo() {
        repository
            .fetchBoardModifyInfo(documentId = documentId)
            .setLoadingState()
            .onEach { _info.value = it }
            .catch {
                it.printStackTrace()
                updateMessage(it.message ?: "오류가 발생하였습니다")
                if (it is BoardException.SearchError) updateFinish(true)
            }
            .launchIn(viewModelScope)
    }

    fun onRegister() {
        if (documentId.isNotEmpty()) {
            updateBoard()
        } else {
            uploadBoard()
        }
    }

    /** 게시글 수정 **/
    private fun updateBoard() {
        repository
            .updateBoard(
                documentId = documentId,
                info = _info.value,
                isImageChange = isImageChange
            )
            .setLoadingState()
            .onEach {
                updateMessage(it)
                updateFinish(true)
            }
            .catch { updateMessage(it.message ?: "수정 실패") }
            .launchIn(viewModelScope)
    }

    /** 게시글 업로드 **/
    private fun uploadBoard()  {
        repository
            .uploadBoard(_info.value)
            .setLoadingState()
            .onEach {
                updateMessage(it)
                updateFinish(true)
            }
            .catch { updateMessage(it.message ?: "등록 실패") }
            .launchIn(viewModelScope)
    }

}