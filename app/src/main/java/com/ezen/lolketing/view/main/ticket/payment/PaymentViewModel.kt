package com.ezen.lolketing.view.main.ticket.payment

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.TicketTemp
import com.ezen.lolketing.repository.PaymentRepository
import com.ezen.lolketing.util.priceFormat
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
) : StatusViewModel() {

    private val _info = MutableStateFlow(TicketTemp.create())
    val info: StateFlow<TicketTemp> = _info

    private val _myCache = MutableStateFlow(0L)
    val myCache: StateFlow<String> = _myCache.map { it.priceFormat() }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val isInsufficientBalance = _myCache.map { it < _info.value.getPrice() }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _purchaseId = MutableStateFlow("")
    val purchaseId: StateFlow<String> = _purchaseId

    fun setInfo(info: TicketTemp) {
        _info.value = info
    }

    /** 사용자 캐시 조회 **/
    fun fetchUserCacheInfo() {
        repository
            .fetchUserCacheInfo()
            .setLoadingState()
            .onEach { _myCache.value = it }
            .catch { updateMessage(it.message ?: "회원 정보 오류") }
            .launchIn(viewModelScope)
    }

    fun buyTickets() {
        if (isInsufficientBalance.value) {
            updateMessage("잔액이 부족합니다. 캐시 충전 후 이용해주세요")
            return
        }

        val data = generateQRCode(_info.value.qrCodeInfo())
            .getOrNull()
            ?: run {
                updateMessage("오류가 발생하였습니다")
                return
            }

        repository
            .buyTickets(
                ticketTemp = _info.value,
                data = data
            )
            .setLoadingState()
            .onEach {
                _purchaseId.value = it
                updateMessage("구매 완료")
                updateFinish(true)
            }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다") }
            .launchIn(viewModelScope)
    }


    /** QR 코드 생성 **/
    private fun generateQRCode(content: String) = runCatching {
        val qrCodeWriter = QRCodeWriter()
        val bitmap = toBitmap(qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200))

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.toByteArray()
    } // generateQRCode()

    /** QR 코드 그리기 **/
    private fun toBitmap(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (matrix.get(x, y))
                    bmp.setPixel(x, y, Color.BLACK)
                else
                    bmp.setPixel(x, y, Color.WHITE)
            }
        }
        return bmp
    } // toBitmap()


}