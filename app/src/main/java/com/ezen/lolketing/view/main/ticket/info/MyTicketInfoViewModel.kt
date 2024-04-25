package com.ezen.lolketing.view.main.ticket.info

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.TicketInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyTicketInfoViewModel @Inject constructor(
//    private val repository: PurchaseRepository
) : StatusViewModel() {

    private val _ticketInfo = MutableStateFlow(TicketInfo.create())
    val ticketInfo : StateFlow<TicketInfo> = _ticketInfo

    private var documentId = ""

    fun setDocumentId(documentId: String) {
        this.documentId = documentId
        fetchTicketInfo()
    }

    fun fetchTicketInfo()  {
//        repository
//            .fetchTicketInfo(documentId)
//            .setLoadingState()
//            .onEach { _ticketInfo.value = it }
//            .catch { updateMessage(it.message ?: "오류 발생") }
//            .launchIn(viewModelScope)
    }

    /** 티켓 환불 **/
    fun updateRefundTicket() {
        if (_ticketInfo.value.isNoRefund()) {
            updateMessage("경기 시작 4시간 전 부터는 환불이 불가능합니다.")
            return
        }

//        repository
//            .updateRefundTicket(
//                ticketInfo = _ticketInfo.value,
//                purchaseDocumentId = documentId
//            )
//            .setLoadingState()
//            .onEach {
//                updateMessage(it)
//                updateFinish(true)
//            }
//            .catch { updateMessage(it.message ?: "오류 발생") }
//            .launchIn(viewModelScope)
    }

}