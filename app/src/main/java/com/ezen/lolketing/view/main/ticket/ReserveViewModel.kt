package com.ezen.lolketing.view.main.ticket

import com.ezen.lolketing.StatusViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReserveViewModel @Inject constructor() : StatusViewModel() {
    private val _uiStatus = MutableStateFlow(ReserveUIStatus())
    val uiStatus: StateFlow<ReserveUIStatus> = _uiStatus

    fun updateStatus(value: ReserveState) {
        val uiStatus = _uiStatus.value
        _uiStatus.value = when (value) {
            ReserveState.ReservationInformation ->
                uiStatus.copy(isReservationInformation = uiStatus.isReservationInformation.not())

            ReserveState.RefundInformation ->
                uiStatus.copy(isRefundInformation = uiStatus.isRefundInformation.not())

            ReserveState.PickUpTicket ->
                uiStatus.copy(isPickUpTicket = uiStatus.isPickUpTicket.not())

            ReserveState.Notice ->
                uiStatus.copy(isNotice = uiStatus.isNotice.not())

            ReserveState.Restrictions ->
                uiStatus.copy(isRestrictions = uiStatus.isRestrictions.not())
        }
    }

}

data class ReserveUIStatus(
    val isReservationInformation: Boolean = false,
    val isRefundInformation: Boolean = false,
    val isPickUpTicket: Boolean = false,
    val isNotice: Boolean = false,
    val isRestrictions: Boolean = false,
)

sealed class ReserveState {
    // 예매 안내
    object ReservationInformation : ReserveState()

    // 환불 안내
    object RefundInformation : ReserveState()

    // 티켓 수령 및 입장
    object PickUpTicket : ReserveState()

    // 유의사항
    object Notice : ReserveState()

    // 관람 시 제한 사항
    object Restrictions : ReserveState()
}