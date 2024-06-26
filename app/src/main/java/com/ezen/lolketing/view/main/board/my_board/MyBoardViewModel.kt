package com.ezen.lolketing.view.main.board.my_board

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

// 제거 예정
@HiltViewModel
class MyBoardViewModel @Inject constructor(
    private val repository: BoardRepository
) : StatusViewModel() {

    private var _team = ""
    val team: String = _team

    private val _list = MutableStateFlow(listOf<BoardItem>())
    val list: StateFlow<List<BoardItem>> = _list

    fun setTeam(team: String) {
        _team = team
    }

    fun fetchListOfMyWritings() = viewModelScope.launch {
        repository
            .fetchListOfMyWritings(_team)
            .setLoadingState()
            .onEach { _list.value = it }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

}