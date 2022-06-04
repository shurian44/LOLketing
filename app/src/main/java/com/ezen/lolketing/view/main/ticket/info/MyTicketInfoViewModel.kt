package com.ezen.lolketing.view.main.ticket.info


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.TicketInfo
import com.ezen.lolketing.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MyTicketInfoViewModel @Inject constructor(
    private val repository: PurchaseRepository
) : BaseViewModel<MyTicketInfoViewModel.Event>() {

    private lateinit var ticketInfo : TicketInfo
    private var refund = 0L

    fun getTicketInfo(
        documentId: String
    ) = viewModelScope.launch {
        repository.getTicketInfo(
            documentId = documentId,
            successListener = {
                ticketInfo = it
                event(Event.Success(it))
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    fun setRefundTicket(
        purchaseDocumentId : String,
        time: String,
        seat: String
    ) = viewModelScope.launch {

        Log.e(TAG, "start")
        repository.deletePurchase(
            documentId = purchaseDocumentId,
            successListener = {
                Log.e(TAG, "success deletePurchase")
            },
            failureListener = {
                Log.e(TAG, "error deletePurchase")
                event(Event.Failure)
                cancel()
            }
        )

        repository.deleteQrCode(
            path = "ticket/${time}_${ticketInfo.gameTitle}_${seat.replace(", ", "_")}.jpg",
            successListener = {
                Log.e(TAG, "success deleteQrCode")
            },
            failureListener = {
                Log.e(TAG, "error deleteQrCode")
                event(Event.Failure)
                cancel()
            }
        )

        repository.updateUserCache(
            refund = refund,
            successListener = {
                Log.e(TAG, "success updateUserCache")
            },
            failureListener = {
                Log.e(TAG, "error updateUserCache")
                event(Event.Failure)
                cancel()
            }
        )

        repository.updateSeatInfo(
            time = time,
            documentIdList = ticketInfo.documentList,
            successListener = {
                Log.e(TAG, "success updateSeatInfo")
            },
            failureListener = {
                Log.e(TAG, "error updateSeatInfo")
                event(Event.Failure)
                cancel()
            }
        )

        Log.e(TAG, "전체 완료")

    }

    fun getRefund(time: String, amount: Int) : Boolean {
        // 2022.06.06 17:00
        val timeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        val ticketTime = timeFormat.parse(time)?.time ?: 0
        val fourHour = 1000L * 60 * 60 * 4
        val currentTime = System.currentTimeMillis()
        val pay = amount * 11_000L

        return when {
            // 환불 불가 : 게임 시작 4시간 전 이후에는 환불 불가
            currentTime > ticketTime - fourHour -> {
                false
            }
            // 수수료 면제 : 당일 구매의 경우 수수료 면제
            // todo 구매일이 없었다..ㅠㅠ
            dateFormat.format(ticketTime) == dateFormat.format(currentTime) -> {
                refund = pay
                true
            }
            // 수수료 10% : 구매 다음 날 부터 게임 시작 4시간 이전의 경우 취소 수수료 10% 발생
            else -> {
                refund = (pay * 0.9).toLong()
                true
            }
        }
    }

    sealed class Event {
        data class Success(val info : TicketInfo) : Event()
        object Failure : Event()
    }

    companion object {
        const val TAG = "MyTicketInfoViewModel"
    }

}