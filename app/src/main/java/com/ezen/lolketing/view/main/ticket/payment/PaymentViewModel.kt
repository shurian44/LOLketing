package com.ezen.lolketing.view.main.ticket.payment

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.PurchaseDTO
import com.ezen.lolketing.repository.PaymentRepository
import com.ezen.lolketing.util.Code
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository,
    private val auth: FirebaseAuth
): BaseViewModel<PaymentViewModel.Event>() {

    /** 사용자 캐시 조회 **/
    fun getUserCache() = viewModelScope.launch {
        event(Event.Loading)
        repository
            .getUserCache(
                successListener = { cache ->
                    event(Event.MyCache(cache))
                },
                failureListener = {
                    event(Event.UserInfoFailure)
                }
            )
    }

    /** 좌석 업데이트 **/
    fun updateSeat(
        firstDocumentId: String,
        secondDocumentIdList: List<String>,
    ) = viewModelScope.launch {
        event(Event.Loading)
        val email = auth.currentUser?.email
        if (email == null) {
            event(Event.Failure)
            return@launch
        }

        secondDocumentIdList.forEach {
            repository.updateSeat(
                firstDocumentId = firstDocumentId,
                secondDocumentId =it,
                data = email,
                onSuccessListener = {
                },
                onFailureListener = {
                    event(Event.Failure)
                }
            )
        }
        event(Event.SeatSuccess)
    }

    /** QR 코드 생성 **/
    fun generateQRCode(content: String) = viewModelScope.launch {
        val qrCodeWriter = QRCodeWriter()
        try{
            val bitmap = toBitmap(qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200))

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            repository.generateQrCode(
                path = "ticket/${content}.jpg",
                data = data,
                onSuccessListener = {
                    event(Event.QrCodeSuccess(it))
                },
                onFailureListener = {
                    event(Event.Failure)
                }
            )

        }catch (e : WriterException){
            e.printStackTrace()
        }
    } // generateQRCode()

    /** QR 코드 그리기 **/
    private fun toBitmap(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for(x in 0 until width){
            for(y in 0 until height){
                if(matrix.get(x, y))
                    bmp.setPixel(x, y, Color.BLACK )
                else
                    bmp.setPixel(x, y, Color.WHITE )
            }
        }
        return bmp
    } // toBitmap()

    /** 구매 내역 추가 **/
    fun setPurchase(
        amount: Int,
        downloadUrl: String,
        team: String,
        price: Long,
        information: String,
        documentList: List<String>
    ) = viewModelScope.launch {
        repository
            .setPurchase(
                data = PurchaseDTO(
                    amount = amount,
                    group = Code.PURCHASE_TICKET.code,
                    id = auth.currentUser?.email,
                    image = downloadUrl,
                    name = team,
                    price = price,
                    status = PurchaseDTO.USABLE,
                    timestamp = System.currentTimeMillis(),
                    information = information,
                    documentList = documentList
                ),
                successListener = {
                    event(Event.PurchaseSuccess(it))
                },
                failureListener = {
                    event(Event.Failure)
                }
            )
    }

    /** 캐시 소모하패 **/
    fun myCacheDeduction(
        price: Long
    ) =  viewModelScope.launch {
        repository
            .myCacheDeduction(
                deductionCache = price,
                successListener = {
                    event(Event.PaymentSuccess)
                },
                failureListener = {
                    event(Event.Failure)
                }
            )
    }

    sealed class Event {
        data class MyCache(val cache: Long) : Event()
        object SeatSuccess : Event()
        data class QrCodeSuccess(
            val downloadUrl : String
        ) : Event()
        data class PurchaseSuccess(
            val documentId : String
        ): Event()
        object UserInfoFailure: Event()
        object PaymentSuccess: Event()
        object Failure : Event()
        object Loading: Event()
    }

}