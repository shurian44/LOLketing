package com.ezen.lolketing.view.main.board

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.model.BoardDTO
import com.ezen.lolketing.repository.BoardRepository
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val repository: BoardRepository
): BaseViewModel<BoardListViewModel.Event>() {

    fun getBoardList(query: String) = viewModelScope.launch {
        repository.getBoardList(query = query)?.let {
            it.addSnapshotListener { querySnapshot, _ ->
                if (querySnapshot == null) {
                    event(Event.BoardList(emptyList()))
                } else {
                    val list = mutableListOf<Board>()
                    querySnapshot.forEach{ snapshot ->
                        // todo 삭제 관련 코드 알아봅시다.
                        snapshot.id
                        list.add(snapshot.toObject(Board::class.java))
                    }
                    event(Event.BoardList(list))
                }
            }
//            event(Event.BoardList(it))
        } ?: error("게시글을 불러오는 과정에서 오류가 발생하였습니다.")
    }

    fun getBoardList(queryList: List<Pair<String, Any>>) = viewModelScope.launch {
        repository.getBoardList(queryList = queryList)?.let {
            it.addSnapshotListener { querySnapshot, _ ->
                if (querySnapshot == null) {
                    event(Event.BoardList(emptyList()))
                } else {
                    val list = mutableListOf<Board>()
                    querySnapshot.forEach{ snapshot ->
                        // todo 삭제 관련 코드 알아봅시다.
                        snapshot.id
                        list.add(snapshot.toObject(Board::class.java))
                    }
                    event(Event.BoardList(list))
                }
            }
//            event(Event.BoardList(it))
        } ?: error("게시글을 불러오는 과정에서 오류가 발생하였습니다.")
    }

    private fun error(message : String) {
        event(Event.Error(message))
    }

    sealed class Event {
        data class BoardList(val list: List<Board>) : Event()
        data class Error(val msg: String) : Event()
    }

}