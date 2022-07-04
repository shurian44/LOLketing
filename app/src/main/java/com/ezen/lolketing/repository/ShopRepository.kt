package com.ezen.lolketing.repository

import com.ezen.lolketing.model.ShopDTO
import com.ezen.lolketing.model.ShopItem
import com.ezen.lolketing.model.ShopListItem
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getShoppingList(
        successListener: (List<ShopListItem>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.getBasicSnapshot(
            collection = Constants.SHOP,
            successListener = { snapshot ->
                val list = snapshot.mapNotNull {
                    it.toObject(ShopDTO::class.java).mapper().also { item ->
                        item?.documentId = it.id
                    }
                }
                successListener(list)
            },
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun getShoppingList(
        query: String,
        successListener: (List<ShopListItem>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.getBasicQuerySnapshot(
            collection = Constants.SHOP,
            field = "group",
            query = query,
            orderByField = "name",
            successListener = { snapshot ->
                val list = snapshot.mapNotNull {
                    it.toObject(ShopDTO::class.java).mapper().also { item ->
                        item?.documentId = it.id
                    }
                }
                successListener(list)
            },
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun getShoppingItem(
        documentId: String,
        successListener: (ShopItem) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.getBasicSnapshot(
            collection = Constants.SHOP,
            document = documentId,
            successListener = {
                it.toObject(ShopDTO::class.java)
                    ?.itemMapper()
                    ?.let(successListener)
                    ?: failureListener()
            },
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }



}