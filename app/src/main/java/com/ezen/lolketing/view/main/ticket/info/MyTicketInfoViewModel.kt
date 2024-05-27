package com.ezen.lolketing.view.main.ticket.info

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.TicketIdParam
import com.ezen.network.model.TicketInfo
import com.ezen.network.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyTicketInfoViewModel @Inject constructor(
    private val repository: PurchaseRepository
) : StatusViewModel() {

    private val _ticketInfo = MutableStateFlow(TicketInfo.init())
    val ticketInfo : StateFlow<TicketInfo> = _ticketInfo

    private val idList = mutableListOf<Int>()

    fun fetchTicketInfo(ids: String)  {
        val idList = runCatching {
            ids.split(",").map { it.toInt() }
        }.getOrDefault(listOf())

        repository
            .fetchTicketInfo(TicketIdParam(idList))
            .setLoadingState()
            .onEach {
                _ticketInfo.value = it
                this.idList.addAll(idList)
            }
            .catch {
                updateMessage(it.message ?: "티켓 정보 오류 발생")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    /** 티켓 환불 **/
    fun updateRefundTicket() {
        repository
            .refundTicket(idList)
            .setLoadingState()
            .onEach {
                updateMessage("환불 완료")
                updateFinish()
            }
            .catch { updateMessage(it.message ?: "환불 오류 발생") }
            .launchIn(viewModelScope)
    }

}