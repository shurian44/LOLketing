package com.ezen.lolketing.repository

import com.ezen.lolketing.database.dao.ShopDao
import com.ezen.lolketing.model.PurchaseDTO
import com.ezen.lolketing.model.PurchaseHistory
import com.ezen.lolketing.model.PurchaseInfo
import com.ezen.lolketing.model.ShopDTO
import com.ezen.lolketing.model.ShopItem
import com.ezen.lolketing.model.ShoppingInfo
import com.ezen.lolketing.model.TicketInfo
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

    fun fetchShoppingList() = flow {
        client
            .getBasicSnapshot(
                collection = Constants.SHOP,
                valueType = ShopDTO::class.java
            )
            .getOrThrow()
            .mapNotNull { (item, documentId) -> item.mapper(documentId) }
            .let { emit(it) }
    }

    fun fetchShoppingDetail(documentId: String) = flow {
        client
            .getBasicSnapshot(
                collection = Constants.SHOP,
                document = documentId,
                valueType = ShopDTO::class.java
            )
            .getOrThrow()
            ?.itemMapper()
            ?.let { emit(it) }
            ?: throw Exception("오류 발생")
    }

    fun insertShoppingBasket(
        item: ShopItem,
        documentId: String
    ) = flow {
        emit(database.insertShoppingBasket(item.mapper(documentId)))
    }

    fun selectBasketCount() = database.selectBasketCount()

    fun fetchPurchaseInfo(
        databaseIdList: List<Long>,
        documentId: String,
        amount: Int
    ) = flow {
        val shoppingInfo = getShoppingInfo()
        val list = mutableListOf<PurchaseInfo>()

        if (documentId.isNotEmpty()) {
            client
                .getBasicSnapshot(
                    collection = Constants.SHOP,
                    document = documentId,
                    valueType = ShopDTO::class.java
                )
                .getOrThrow()
                ?.purchaseMapper(
                    amount = amount,
                    documentId = documentId,
                )
                ?.let { list.add(it) }
                ?: throw Exception("오류 발생")
        } else {
            database
                .selectShoppingBasketList(databaseIdList)
                .map { it.mapper() }
                .let { list.addAll(it) }
        }
        emit(Pair(shoppingInfo, list))
    }

    private suspend fun getShoppingInfo() =
        client
            .getUserInfo()
            .getOrNull()
            ?.mapperShoppingInfo()
            ?: throw LoginException.EmptyInfo

    fun fetchBasketList() = flow {
        emit(
            database
                .selectAllShoppingBasket()
                .map { it.mapper() }
        )
    }

    suspend fun updateBasketChecked(id: Long, isChecked: Boolean) =
        database.updateBasketChecked(id, isChecked)

    private suspend fun deleteBasketItems(idList: List<Long>) =
        database.deleteBasketItems(idList)

    fun setPurchaseItems(
        list: List<PurchaseInfo>,
        userInfo: ShoppingInfo,
        idList: List<Long>
    ) = flow {
        list.mapNotNull { it.mapper(userInfo) }.forEach {
            client
                .basicAddData(
                    collection = Constants.PURCHASE,
                    data = it
                )
                .getOrThrow()
        }

        val result = list.sumOf { it.price }
        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = userInfo.id,
                updateData = mapOf("cache" to FieldValue.increment(result * -1))
            )
            .getOrThrow()

        deleteBasketItems(idList)


        emit("결제 완료")
    }

    fun fetchPurchaseHistory() = flow {
        val email = client.getUserEmail() ?: throw LoginException.EmptyInfo
        val list = mutableListOf<PurchaseHistory>()
        client
            .getBasicQuerySnapshot(
                collection = Constants.PURCHASE,
                field = "id",
                query = email,
                valueType = PurchaseDTO::class.java
            )
            .getOrThrow()
            .mapNotNull { (item, documentId) -> item.historyMapper(documentId) }
            .groupBy { it.date }
            .map { (date, historyList) ->
                list.add(PurchaseHistory.PurchaseDate(date))
                list.addAll(historyList)
            }

        emit(list.toList())
    }

    companion object {
        const val CACHE = "cache"
        const val DOCUMENT_ID = "documentId"
        const val RESERVE_ID = "reserveId"
    }

}