package com.ezen.lolketing.repository

import com.ezen.lolketing.model.CacheModifyUser
import com.ezen.lolketing.model.PurchaseDTO
import com.ezen.lolketing.model.TicketTemp
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.LoginException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val client: FirebaseClient
) {

    fun fetchUserCacheInfo() = flow {
        client
            .getUserInfo()
            .getOrThrow()
            .cache
            ?.let { emit(it) }
            ?: throw LoginException.EmptyUser
    }

    fun fetchCacheDetailInfo() = flow {
        emit(
            client
                .getUserInfo()
                .getOrThrow()
                .mapperCacheModify()
                ?: throw LoginException.EmptyInfo
        )
    }

    fun buyTickets(
        ticketTemp: TicketTemp,
        data: ByteArray,
    ) = flow {
        val email = client.getUserEmail() ?: throw LoginException.EmptyUser

        ticketTemp.documentList.forEach {
            updateSeats(time = ticketTemp.time, documentId = it, email = email)
        }

        val image = generateQrCode(path = ticketTemp.qrCodeInfo(), data = data)

        val documentId = addPurchase(
            ticketTemp.toPurchaseDTO(
                id = email,
                image = image
            )
        ).id

        myCacheDeduction(
            email = email,
            deductionCache = ticketTemp.getPrice()
        )

        emit(documentId)
    }

    private suspend fun updateSeats(
        time: String,
        documentId: String,
        email: String
    ) = client
        .doubleUpdateData(
            firstCollection = Constants.GAME,
            firstDocument = time,
            secondCollection = Constants.SEAT,
            secondDocument = documentId,
            updateData = mapOf("reserveId" to email)
        )
        .getOrThrow()

    private suspend fun generateQrCode(
        path: String,
        data: ByteArray
    ) = client
            .basicFileUpload(
                fileName = path,
                data = data
            )
            .getOrThrow()

    private suspend fun addPurchase(
        data: PurchaseDTO
    ) = client
            .basicAddData(
                collection = Constants.PURCHASE,
                data = data,
            )
            .getOrThrow()

    private suspend fun myCacheDeduction(
        email: String,
        deductionCache: Long
    ) = client
        .basicUpdateData(
            collection = Constants.USERS,
            documentId = email,
            updateData = mapOf(
                CACHE to FieldValue.increment(-deductionCache),
                ROULETTE_COUNT to FieldValue.increment(if (deductionCache > 11_000) 2 else 1)
            )
        )

    fun updateChargingCache(info: CacheModifyUser) = flow {
        val email = client.getUserEmail() ?: throw LoginException.EmptyInfo

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = email,
                updateData = mapOf(
                    CACHE to info.myCache,
                    POINT to info.point,
                    GRADE to info.grade
                )
            )

        emit("충전 완료")
    }

    companion object {
        const val CACHE = "cache"
        const val POINT = "point"
        const val GRADE = "grade"
        const val ROULETTE_COUNT = "rouletteCount"
    }

}