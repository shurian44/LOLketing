package com.ezen.lolketing.view.main.board.search

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.repository.BoardRepository
import com.ezen.lolketing.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: BoardRepository
) : StatusViewModel() {

    var team = ""
        private set

    var query = ""
        private set

    var isTitleSearch = true
        private set

    private val _list = MutableStateFlow(listOf<BoardItem>())
    val list: StateFlow<List<BoardItem>> = _list

    fun setTeam(team: String) {
        this.team = team
    }

    /** 검색 내용 조회 **/
    fun getSearchBoardList() {
        repository
            .fetchBoardList(
                team = team,
                filed = if (isTitleSearch) Constants.TITLE else Constants.NICKNAME,
                query = query
            )
            .setLoadingState()
            .onEach { _list.value = it }
            .catch {
                it.printStackTrace()
                updateMessage(it.message ?: "오류가 발생하였습니다")
            }
            .launchIn(viewModelScope)
    }

    fun onSearch(
        isTitleSearch: Boolean,
        query: String
    ) {
        this.isTitleSearch = isTitleSearch
        this.query = query

        getSearchBoardList()
    }

}