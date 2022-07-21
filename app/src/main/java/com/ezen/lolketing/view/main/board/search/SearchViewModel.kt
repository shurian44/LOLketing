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

    var field: String = SearchActivity.TITLE
    var searchText: String = ""
    private var team: String = ""

    /** 검색 내용 조회 **/
    fun getSearchBoardList() = viewModelScope.launch {
        event(Event.Loading)
        repository.getSearchBoardList(
            field = field,
            data = searchText,
            team = team,
            successListener = {
                event(Event.Success(it))
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    /** 검색 조건 세팅 **/
    fun setSearchData(field: String, searchText: String, team: String) {
        this.field = field
        this.searchText = searchText
        this.team = team
    }

    sealed class Event {
        object Loading: Event()
        object Failure: Event()
        data class Success(
            val list : List<BoardItem.BoardListItem>
        ): Event()
    }

}