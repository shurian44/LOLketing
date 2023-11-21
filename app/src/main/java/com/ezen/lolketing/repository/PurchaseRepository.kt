package com.ezen.lolketing.repository

import com.ezen.lolketing.database.dao.ShopDao
import com.ezen.lolketing.database.entity.ShopEntity
import com.ezen.lolketing.model.*
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class PurchaseRepository @Inject constructor(
    private val client: FirebaseClient,
    private val database: ShopDao
) {

    suspend fun getTicketInfo(
        documentId: String,
        successListener: (TicketInfo) -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client
//            .getBasicSnapshot(
//                collection = Constants.PURCHASE,
//                document = documentId,
//                successListener = {
//                    it.toObject(PurchaseDTO::class.java)?.let { purchase ->
//                        successListener(purchase.ticketMapper())
//                    } ?: failureListener()
//                },
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun updateUserCache(
        refund: Long,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
//        val email = client.getUserEmail() ?: throw Exception("email is null")
//
//        client
//            .basicUpdateData(
//                collection = Constants.USERS,
//                documentId = email,
//                updateData = mapOf(CACHE to FieldValue.increment(refund)),
//                successListener = successListener,
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun updateSeatInfo(
        time: String,
        documentIdList: List<String>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
//        documentIdList.forEach {
//            client
//                .doubleUpdateData(
//                    firstCollection = Constants.GAME,
//                    firstDocument = time,
//                    secondCollection = Constants.SEAT,
//                    secondDocument = it,
//                    updateData = mapOf(DOCUMENT_ID to "", RESERVE_ID to ""),
//                    successListener = {},
//                    failureListener = failureListener
//                )
//        }
//        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun deletePurchase(
        documentId: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client
//            .basicDelete(
//                collection = Constants.PURCHASE,
//                documentId = documentId,
//                successListener = successListener,
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun deleteQrCode(
        path: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ): Any? = try {
//        client
//            .storageDelete(
//                path = path,
//                successListener = successListener,
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
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