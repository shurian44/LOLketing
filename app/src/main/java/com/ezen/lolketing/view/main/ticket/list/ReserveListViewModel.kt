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

    fun isMasterUser() = viewModelScope.launch {
        event(Event.UserGrade(isMaster = repository.isMasterUser()))
    }

    fun addNewGame(
        date: String,
        time: String
    ) = viewModelScope.launch {
        repository.addNewGame(
            date = date,
            time = time,
            successListener = {
                event(Event.NewGameAddSuccess("$date $time"))
            },
            failureListener = {
                event(Event.NewGameAddFailure)
            }
        )
    }

    fun setReservedSeat(
        time: String
    ) = viewModelScope.launch {
        repository.setReservedSeat(
            documentId = time,
            successListener = {
                event(Event.ReserveSeatAddSuccess)
            }
        )
    }

    sealed class Event {
        object EmptyList : Event()
        data class GameList(
            val list : List<Ticket>
        ) : Event()
        data class UserGrade(val isMaster: Boolean) : Event()
        data class NewGameAddSuccess(
            val time: String
        ) : Event()
        object NewGameAddFailure : Event()
        object ReserveSeatAddSuccess : Event()
    }

}