package com.ezen.lolketing.view.main.board.search

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: BoardRepository
) : BaseViewModel<SearchViewModel.Event>() {

    private var field: String = BOARD_TITLE

    fun getSearchBoardList(data: String) = viewModelScope.launch {
        event(Event.Loading)
        repository.getSearchBoardList(
            field = field,
            data = data,
            successListener = {
                event(Event.Success(it))
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    fun setChangeField(field: String) {
        this.field = field
    }

    sealed class Event {
        object Loading: Event()
        object Failure: Event()
        data class Success(
            val list : List<BoardItem.BoardListItem>
        ): Event()
    }

    companion object {
        const val BOARD_TITLE = "title"
        const val WRITER = "nickname"
    }

}