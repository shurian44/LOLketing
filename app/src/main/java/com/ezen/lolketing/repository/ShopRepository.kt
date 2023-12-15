package com.ezen.lolketing.repository

import com.ezen.lolketing.database.dao.ShopDao
import com.ezen.lolketing.database.entity.ShopEntity
import com.ezen.lolketing.model.ShopDTO
import com.ezen.lolketing.model.ShopItem
import com.ezen.lolketing.model.ShopListItem
import com.ezen.lolketing.model.mapper
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val client: FirebaseClient,
    private val db: ShopDao
) {

    suspend fun getShoppingList(
        successListener: (List<ShopListItem>) -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.getBasicSnapshot(
//            collection = Constants.SHOP,
//            successListener = { snapshot ->
//                val list = snapshot.mapNotNull {
//                    it.toObject(ShopDTO::class.java).mapper().also { item ->
//                        item?.documentId = it.id
//                    }
//                }
//                successListener(list)
//            },
//            failureListener = failureListener
//        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
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

    suspend fun getShoppingItem(
        documentId: String,
        successListener: (ShopItem) -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.getBasicSnapshot(
//            collection = Constants.SHOP,
//            document = documentId,
//            successListener = {
//                it.toObject(ShopDTO::class.java)
//                    ?.itemMapper(it.id)
//                    ?.let(successListener)
//                    ?: failureListener()
//            },
//            failureListener = failureListener
//        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun insertShoppingBasket(
        item: ShopItem,
        count: Int,
        timestamp: Long,
        listener: (Long) -> Unit
    ) {
        val result = db.insertShoppingBasket(
            item.mapper(count)
        )
        listener(result)
    }

    fun selectBasketCount() = db.selectBasketCount()

}