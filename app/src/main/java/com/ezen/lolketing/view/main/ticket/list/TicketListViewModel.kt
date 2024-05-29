package com.ezen.lolketing.view.main.ticket.list

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.view.custom.TicketItem
import com.ezen.network.model.Game
import com.ezen.network.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketListViewModel @Inject constructor(
    private val repository: PurchaseRepository
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<TicketItem>())
    val list: StateFlow<List<TicketItem>> = _list

    /** 경기 일정 조회 **/
    fun fetchGameList() = viewModelScope.launch {
        repository
            .fetchGameList()
            .setLoadingState()
            .onEach {
                _list.value = it.map { game -> game.toTicketItem() }
            }
            .catch {
                updateMessage(it.message ?: "게임 정보를 찾지 못하였습니다.")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    private fun Game.toTicketItem() = TicketItem(
        id = gameId.toString(),
        leftTeam = leftTeam,
        rightTeam = rightTeam,
        message = getDateFormat(),
        status = when {
            isSoldOut -> TicketItem.SOLD_OUT
            isDateExpired() -> TicketItem.FINISH
            else -> TicketItem.POSSIBLE
        }
    )
}