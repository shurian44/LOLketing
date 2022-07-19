package com.ezen.lolketing.view.main.ticket.detail

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.SeatItem
import com.ezen.lolketing.repository.TicketingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HallViewModel @Inject constructor(
    private val repository: TicketingRepository
) : BaseViewModel<HallViewModel.Event>() {

    /** 좌석 정보 조회 **/
    fun getSeatList(documentId : String, selectHall: String) = viewModelScope.launch {
        repository.getReservedSeat(
            documentId = documentId,
            hall = selectHall,
            onSuccessListener = {
                event(Event.Success(it))
            },
            onFailureListener = {
                event(Event.Failure)
            }
        )
    }

    sealed class Event {
        data class Success(
            val list : List<SeatItem>
        ) : Event()
        object Failure : Event()
    }

}