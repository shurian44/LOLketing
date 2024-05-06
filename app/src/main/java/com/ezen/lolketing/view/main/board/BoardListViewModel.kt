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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val repository: BoardRepository
): StatusViewModel() {

    private val _item = MutableStateFlow(BoardListItem())
    val item: StateFlow<BoardListItem> = _item

    init {
        fetchBoardList()
    }

    fun fetchBoardList() {
        val value = _item.value
        if (value.isLoading || value.isMoreData.not()) return

        repository
            .fetchBoardList(
                skip = value.skip,
                limit = value.limit,
                teamId = value.team.teamId
            )
            .onStart {
                startLoading()
                _item.update { it.copy(isLoading = true) }
            }
            .onEach { newList ->
                val isMoreData = newList.size == value.limit
                _item.value = value.copy(
                    list = value.list + newList,
                    isMoreData = isMoreData,
                    skip = value.skip + value.limit
                )
            }
            .onCompletion {
                endLoading()
                _item.update { it.copy(isLoading = false) }
            }
            .catch { updateMessage(it.message ?: "게시글 조회 실패") }
            .launchIn(viewModelScope)
    }

    fun updateTeam(team: Team) {
        if (_item.value.team == team)
            return

        _item.update {
            it.copy(
                list = listOf(),
                isMoreData = true,
                team = team,
                skip = 0
            )
        }
        fetchBoardList()
    }

    fun updateLike(boardId: Int) {
        repository
            .updateBoardLike(boardId)
            .setLoadingState()
            .onEach { board ->
                _item.update { item ->
                    item.copy(
                        list = item.list.map {
                            if (it.id == boardId) board else it
                        }
                    )
                }
            }
            .catch { updateMessage(it.message ?: "좋아요 실패") }
            .launchIn(viewModelScope)
    }

}

data class BoardListItem(
    val list: List<Board> = listOf(),
    val isMoreData: Boolean = true,
    val team: Team = Team.ALL,
    val skip: Int = 0,
    val limit: Int = 10,
    val isLoading: Boolean = false
)