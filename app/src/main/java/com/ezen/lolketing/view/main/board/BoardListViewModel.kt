package com.ezen.lolketing.view.main.board

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.repository.BoardRepository
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val repository: BoardRepository
): BaseViewModel<BoardListViewModel.Event>() {

    fun getBoardList(query: String) = viewModelScope.launch {
        repository.getBoardList(
            query = query,
            successListener = {
                getBoardListResult(it)
            },
            failureListener = {
                error("게시글을 불러오는 과정에서 오류가 발생하였습니다.")
            }
        ) ?: error("게시글을 불러오는 과정에서 오류가 발생하였습니다.")
    }

    fun getBoardList(queryList: List<Pair<String, Any>>) = viewModelScope.launch {
        repository.getBoardList(
            queryList = queryList,
            successListener = {
                getBoardListResult(it)
            },
            failureListener = {
                error("게시글을 불러오는 과정에서 오류가 발생하였습니다.")
            }
        ) ?: error("게시글을 불러오는 과정에서 오류가 발생하였습니다.")
    }

    private fun getBoardListResult(snapshot: QuerySnapshot) {
        val list = mutableListOf<Board>()
        snapshot.forEach{
            list.add(
                it.toObject(Board::class.java).also { board ->
                    board.documentId = it.id
                }
            )
        }
        event(Event.BoardList(list))
    }

    fun deleteBoard(documentId: String) = viewModelScope.launch {
        repository.deleteBoard(
            documentId = documentId,
            successListener = {
                event(Event.DeleteSuccess)
            },
            failureListener = {
                error("글 삭제를 실패하였습니다.")
            }
        )
    }

    private fun error(message : String) {
        event(Event.Error(message))
    }

    sealed class Event {
        data class BoardList(val list: List<Board>) : Event()
        object DeleteSuccess : Event()
        data class Error(val msg: String) : Event()
    }

}