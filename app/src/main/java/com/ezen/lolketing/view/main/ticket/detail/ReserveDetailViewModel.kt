package com.ezen.lolketing.view.main.ticket.detail

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.ReservationInfo
import com.ezen.network.model.ReservationTicketItem
import com.ezen.network.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ReserveDetailViewModel @Inject constructor(
    private val repository: PurchaseRepository
) : StatusViewModel() {

    private var gameId = 0
    private val _info = MutableStateFlow(ReservationInfo.init())
    val info: StateFlow<ReservationInfo> = _info

    private val _numberOfPeople = MutableStateFlow(1)
    val numberOfPeople: StateFlow<Int> = _numberOfPeople

    private val _uiStatus = MutableStateFlow<Reservation>(Reservation.Init)
    val uiStatus: StateFlow<Reservation> = _uiStatus

    fun fetchReservedSeat(gameId: Int) {
        repository
            .fetchReservedSeats(gameId = gameId)
            .setLoadingState()
            .onEach {
                this.gameId = gameId
                _info.value = _info.value.setItem(it)
            }
            .catch {
                updateMessage(it.message ?: "티켓 정보 오류")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    fun changePerson(value: Int) {
        if (value == _numberOfPeople.value) return

        _numberOfPeople.value = value
        _info.value = _info.value.copy(selectedSeats = listOf())
    }

    fun updateSeatState(number: String) {
        val info = _info.value
        val selectedSeats = info.selectedSeats.toMutableList()

        if (info.reservedSeats.contains(number)) {
            updateMessage("예매된 좌석입니다.")
            return
        }

        if (selectedSeats.contains(number)) {
            selectedSeats.remove(number)
            _info.value = _info.value.copy(selectedSeats = selectedSeats)
            return
        }

        if (selectedSeats.size >= _numberOfPeople.value) {
            updateMessage("선택한 인원수를 확인해 주세요.")
            return
        }

        selectedSeats.add(number)
        selectedSeats.sort()
        _info.value = _info.value.copy(selectedSeats = selectedSeats)
    }

    fun makeReservation() {
        val info = _info.value
        if (_numberOfPeople.value != info.selectedSeats.size) {
            updateMessage("선택한 인원수를 확인해 주세요.")
            return
        }

        if (info.cash < info.getTicketPrice()) {
            _uiStatus.value = Reservation.CashCharging(info.getCashFormat())
            return
        }

        repository
            .reservationTicket(
                ReservationTicketItem(
                    gameId = info.gameId,
                    userId = info.userId,
                    count = _numberOfPeople.value,
                    seatNumber = info
                        .getSelectedSeatsInfo()
                        .replace("A홀", "")
                        .replace(" ", "")
                )
            )
            .onEach { _uiStatus.value = Reservation.Success(it) }
            .catch { updateMessage(it.message ?: "티켓 예매에 실패하였습니다.") }
            .launchIn(viewModelScope)
    }

    fun cashCharging(cash: Int) {
        if (gameId == 0) {
            updateMessage("경기 정보가 없습니다.")
            return
        }

        repository
            .cashChargingAndReservationInfo(cash = cash, gameId = gameId)
            .onEach { _info.value = _info.value.setItem(it) }
            .catch { error -> error.message?.let { updateMessage(it) } }
            .launchIn(viewModelScope)
    }

    fun updateInit() {
        _uiStatus.value = Reservation.Init
    }

}

sealed class Reservation {
    object Init: Reservation()

    data class CashCharging(
        val myCash: String
    ): Reservation()

    data class Success(
        val ids: String
    ) : Reservation()
}