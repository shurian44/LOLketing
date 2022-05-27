package com.ezen.lolketing.view.main.ticket.list

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Ticket
import com.ezen.lolketing.repository.TicketingRepository
import com.ezen.lolketing.util.timestampToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReserveListViewModel @Inject constructor(
    private val repository: TicketingRepository
) : BaseViewModel<ReserveListViewModel.Event>() {

    private fun getYesterdayDateTime() : String =
        (System.currentTimeMillis() - (1000 * 60 * 60 * 24)).timestampToString("yyyy.MM.dd")

    fun getGameList() = viewModelScope.launch {
        repository.getGameList(
            date = getYesterdayDateTime(),
            onSuccessListener = {
                if (it.isEmpty()) {
                    event(Event.EmptyList)
                } else {
                    event(Event.GameList(it))
                }
            },
            onFailureListener = {
                event(Event.EmptyList)
            }
        )
    }

    sealed class Event {
        object EmptyList : Event()
        data class GameList(
            val list : List<Ticket>
        ) : Event()
    }

}