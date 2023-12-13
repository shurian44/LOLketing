package com.ezen.lolketing.repository

import com.ezen.lolketing.database.dao.ShopDao
import com.ezen.lolketing.database.entity.ShopEntity
import com.ezen.lolketing.model.*
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.LoginException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PurchaseRepository @Inject constructor(
    private val client: FirebaseClient,
    private val database: ShopDao
) {

    fun fetchTicketInfo(documentId: String) = flow {
        client
            .getBasicSnapshot(
                collection = Constants.PURCHASE,
                document = documentId,
                valueType = PurchaseDTO::class.java
            )
            .getOrThrow()
            ?.ticketMapper()
            ?.let { emit(it) }
            ?: throw Exception("오류 발생")
    }

    fun updateRefundTicket(
        ticketInfo: TicketInfo,
        purchaseDocumentId: String
    ) = flow {
        deletePurchase(purchaseDocumentId)
        deleteQrCode(ticketInfo.getQrCodeName())
        updateCache(ticketInfo.getRefund())
        updateSeatInfo(
            time = ticketInfo.getTime(),
            documentIdList = ticketInfo.documentList
        )
        emit("환불 완료")
    }

    private suspend fun deletePurchase(documentId: String) {
        client
            .basicDelete(
                collection = Constants.PURCHASE,
                documentId = documentId
            )
    }

    private suspend fun deleteQrCode(path: String) {
        client
            .storageDelete(path = path)
    }

    private suspend fun updateCache(refund: Long) {
        val email = client.getUserEmail() ?: throw LoginException.EmptyInfo

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = email,
                updateData = mapOf(CACHE to FieldValue.increment(refund))
            )
    }

    private suspend fun updateSeatInfo(
        time: String,
        documentIdList: List<String>
    ) {
        documentIdList.forEach {
            client
                .doubleUpdateData(
                    firstCollection = Constants.GAME,
                    firstDocument = time,
                    secondCollection = Constants.SEAT,
                    secondDocument = it,
                    updateData = mapOf(RESERVE_ID to "")
                )
        }
    }

    fun selectAllShoppingBasket() =
        database.selectAllShoppingBasket()

    fun selectShoppingBasketList(idList: List<Long>) =
        database.selectShoppingBasketList(idList)

    suspend fun updateBasketChecked(id: Long, isChecked: Boolean) =
        database.updateBasketChecked(id, isChecked)

    suspend fun deleteBasketItems(idList: List<Long>) =
        database.deleteBasketItems(idList)

    suspend fun getUserInfo(
        successListener: (ShippingInfo) -> Unit,
        failureListener: () -> Unit
    ) {
//        client.getUserInfo(
//            successListener = {
//                it.mapperShippingInfo()?.let(successListener) ?: failureListener()
//            },
//            failureListener = failureListener
//        )
    }

    suspend fun setPurchaseItems(
        list: List<ShopEntity>,
        userInfo: ShippingInfo,
        message: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        list.forEach {
            val item = PurchaseDTO(
                id = userInfo.id,
                address = userInfo.address,
                amount = it.count,
                group = it.group,
                image = it.image,
                information = it.documentId,
                message = message,
                price = it.price,
                name = it.name,
                status = null,
                timestamp = System.currentTimeMillis()
            )

//            client
//                .basicAddData(
//                    collection = Constants.PURCHASE,
//                    data = item,
//                    failureListener = {
//                        failureListener()
//                        return@basicAddData
//                    }
//                )

        }

        val result = list.sumOf { it.price }
//        client
//            .basicUpdateData(
//                collection = Constants.USERS,
//                documentId = userInfo.id,
//                updateData = mapOf("cache" to FieldValue.increment(result * -1)),
//                successListener = {},
//                failureListener = failureListener
//            )

        successListener()

    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun getPurchaseHistoryList(
        successListener: (List<PurchaseHistory>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        val id = client.getUserEmail() ?: throw Exception("email is null")

//        client
//            .getBasicQuerySnapshot(
//                collection = Constants.PURCHASE,
//                field = "id",
//                query = id,
//                successListener = { snapshot ->
//                    val result = snapshot.mapNotNull {
//                        it.toObject(PurchaseDTO::class.java)
//                            .historyMapper(it.id)
//                    }
//                    successListener(result.mapper())
//                },
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    companion object {
        const val CACHE = "cache"
        const val DOCUMENT_ID = "documentId"
        const val RESERVE_ID = "reserveId"
    }

}