package com.ezen.lolketing.view.main.ticket.detail

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.SeatItem
import com.ezen.lolketing.repository.TicketingRepository
import com.ezen.lolketing.util.priceFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ReserveDetailViewModel @Inject constructor(
    private val repository: TicketingRepository
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<SeatItem>())
    val list: StateFlow<List<SeatItem>> = _list

    private val _reserveTime = MutableStateFlow("")
    val reserveTime: StateFlow<String> = _reserveTime

    private val _isOnePerson = MutableStateFlow(true)
    val isOnePerson: StateFlow<Boolean> = _isOnePerson

    private val _ticketPrice = 11_000L
    val ticketPrice: StateFlow<String> = _isOnePerson.flatMapLatest { isOnePerson ->
        if (isOnePerson) {
            MutableStateFlow(_ticketPrice.priceFormat())
        } else {
            MutableStateFlow((_ticketPrice * 2).priceFormat())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500L),
        initialValue = _ticketPrice.priceFormat()
    )

    val selectSeatItem: StateFlow<String> = _list.flatMapLatest { list ->
        MutableStateFlow(
            list.filter { it.checked }.joinToString(", ") { "A홀 ${it.seatNum}" }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500L),
        initialValue = _ticketPrice.priceFormat()
    )

    fun setReserveTime(reserveTime: String) {
        _reserveTime.value = reserveTime
    }

    fun chanePerson(isOnePerson: Boolean) {
        _isOnePerson.value = isOnePerson
        _list.update {
            it.map { item -> item.copy(checked = false) }
        }
    }

    fun updateSeatState(seatItem: SeatItem) {
        val count = if (_isOnePerson.value) 1 else 2
        val checkedCount = _list.value.count { it.checked }
        if (seatItem.checked.not() && checkedCount >= count) {
            updateMessage("인원수를 확인해 주세요")
            return
        }

        _list.update {
            it.map { item ->
                if (seatItem.documentId == item.documentId) {
                    item.copy(checked = seatItem.checked.not())
                } else {
                    item
                }
            }
        }
    }

    fun fetchReservedSeat() {
        repository
            .fetchReservedSeat(documentId = _reserveTime.value, hall = "A홀")
            .setLoadingState()
            .onEach { _list.value = it }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }

}