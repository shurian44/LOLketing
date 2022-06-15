package com.ezen.lolketing.view.main.board.write

import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.model.BoardWriteInfo
import com.ezen.lolketing.repository.BoardRepository
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.Team
import com.ezen.lolketing.util.findCode
import com.ezen.lolketing.view.main.board.BoardListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardWriteViewModel @Inject constructor(
    private val repository: BoardRepository,
    private val pref: SharedPreferences
) : BaseViewModel<BoardWriteViewModel.Event>() {

    fun getBoard(
        documentId: String
    ) = viewModelScope.launch {
        repository.getBoard(
            documentId = documentId,
            successListener = {
                event(Event.WriteInfo(it))
            },
            failureListener = {
                error("게시판 조회를 실패하였습니다.")
            }
        )
    }

    fun uploadImage(
        uri: Uri
    ) = viewModelScope.launch {
        repository
            .uploadImage(
                uri = uri,
                successListener = {
                    event(Event.ImageUploadSuccess(it))
                },
                failureListener = {
                    error("이미지 업로드에 실패하였습니다.")
                }
            )
    }

    fun updateBoard(
        documentId: String,
        updateData: Map<String, Any>
    ) = viewModelScope.launch {

        repository.updateBoard(
            documentId = documentId,
            updateData = updateData,
            successListener = {
                event(Event.UpdateSuccess)
            },
            failureListener = {
                error("게시글 수정 중 오류가 발생하였습니다.")
            }
        )
    }

    fun uploadBoard(
        title: String,
        content: String,
        category: String,
        image: String?,
        team: String?
    ) = viewModelScope.launch {

        val nickName = repository.getUserNickname()
        val email = pref.getString(Constants.ID, "")

        if (nickName.isNullOrEmpty() || email.isNullOrEmpty()) {
            error("유저 정보 조회 중 오류가 발생하였습니다.")
            return@launch
        }

        val board = Board(
            title = title,
            content = content,
            category = findCode(category),
            image = image,
            email = email,
            nickname = nickName,
            team = team,
            timestamp = System.currentTimeMillis(),
            commentCounts = 0,
            likeCounts = 0,
            views = 0
        )

        repository.uploadBoard(
            board,
            successListener = {
                event(Event.UploadSuccess)
            },
            errorListener = {
                error("파일 업로드 과정 중 오류가 발생하였습니다.")
            }
        )
    }

    fun getUserInfo() {
        repository
    }

    private fun error(message : String) {
        event(Event.Error(message))
    }

    sealed class Event {
        data class WriteInfo(val info : BoardWriteInfo) : Event()
        data class UserNickName(val nickName: String) : Event()
        data class Error(val msg: String) : Event()
        data class ImageUploadSuccess(
            val downloadUri : String
        ) : Event()
        object UploadSuccess : Event()
        object UpdateSuccess : Event()
    }

}