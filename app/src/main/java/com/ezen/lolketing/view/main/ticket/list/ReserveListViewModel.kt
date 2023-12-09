package com.ezen.lolketing.view.main.ticket.list

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.Ticket
import com.ezen.lolketing.repository.TicketingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReserveListViewModel @Inject constructor(
    private val repository: TicketingRepository
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<Ticket>())
    val list: StateFlow<List<Ticket>> = _list

    /** 경기 일정 조회 **/
    fun fetchGameList() = viewModelScope.launch {
        repository
            .fetchGameList()
            .setLoadingState()
            .onEach { _list.value = it }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }
}