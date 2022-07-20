package com.ezen.lolketing.model

import com.ezen.lolketing.database.entity.ShopEntity

data class ShopDTO(
    var images : List<String>?= null,
    var price : Long ?= 0,
    var name : String ?= null,
    var group : String ?= null
) {
    fun mapper() : ShopListItem? {
        return ShopListItem(
            image = images?.get(0) ?: return null,
            price = price ?: return null,
            name = name ?: return null,
            group = group ?: return null
        )
    }

    fun itemMapper(documentId: String) : ShopItem? {
        return ShopItem(
            images = images?: return null,
            price = price ?: return null,
            totalPrice = price ?: return null,
            name = name ?: return null,
            group = group ?: return null,
            documentId = documentId
        )
    }
}

data class ShopListItem(
    val image : String,
    val price : Long,
    val name : String,
    val group : String,
    var documentId : String = ""
)

data class ShopItem(
    val images : List<String>,
    val price : Long,
    var totalPrice: Long,
    val name : String,
    val group : String,
    val documentId: String
)

fun ShopItem.mapper(count: Int) = ShopEntity(
        group = group,
        name = name,
        price = totalPrice,
        image = images[0],
        count = count,
        documentId = documentId,
        timestamp = System.currentTimeMillis()
    )
