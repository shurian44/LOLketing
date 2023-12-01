package com.ezen.lolketing.view.main.board

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val repository: BoardRepository
): StatusViewModel() {

    private val _team = MutableStateFlow("")
    val team: StateFlow<String> = _team

    private val _list = MutableStateFlow(mutableListOf<BoardItem>())
    val list: StateFlow<List<BoardItem>> = _list

    fun setTeam(team: String) {
        _team.value = team
        setTopItem()
    }

    private fun setTopItem() {
        _list.value.add(BoardItem.TeamImage(_team.value))
    }

    /** 게시글 조회 **/
    fun fetchBoardList() = viewModelScope.launch {
        repository
            .fetchBoardList(query = _team.value)
            .setLoadingState()
            .onEach {
                val list = mutableListOf<BoardItem>(BoardItem.TeamImage(_team.value))
                list.addAll(it)
                _list.value = list
            }
            .catch { it.printStackTrace() }
            .launchIn(viewModelScope)
    }

}