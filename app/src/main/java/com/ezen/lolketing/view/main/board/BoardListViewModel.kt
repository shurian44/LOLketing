package com.ezen.lolketing.view.main.board

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.Board
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
class BoardListViewModel @Inject constructor(
    private val repository: BoardRepository
): StatusViewModel() {

    private val _item = MutableStateFlow(BoardListItem())
    val item: StateFlow<BoardListItem> = _item

    private var skip = 0
    private var limit = 10

    init {
        fetchBoardList()
    }

    fun fetchBoardList() {
        repository
            .fetchBoardList(skip, limit)
            .setLoadingState()
            .onEach { newList ->
                _item.value = _item.value.copy(list = _item.value.list + newList)
            }
            .catch { updateMessage(it.message ?: "게시글 조회 실패") }
            .launchIn(viewModelScope)
    }

    fun updateTeam(team: Team) {
        _item.update { it.copy(team = team) }
    }
}

data class BoardListItem(
    val list: List<Board> = listOf(),
    val team: Team = Team.ALL
)