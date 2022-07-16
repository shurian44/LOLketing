package com.ezen.lolketing.view.main.board

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val repository: BoardRepository
): BaseViewModel<BoardListViewModel.Event>() {

    /** 게시글 조회 **/
    fun getBoardList(query: String) = viewModelScope.launch {
        repository.getBoardList(
            query = query,
            successListener = {
                event(Event.BoardList(it))
            },
            failureListener = {
                event(Event.Error)
            }
        ) ?: event(Event.Error)
    }

    sealed class Event {
        data class BoardList(val list: List<BoardItem.BoardListItem>) : Event()
        object Error : Event()
    }

}