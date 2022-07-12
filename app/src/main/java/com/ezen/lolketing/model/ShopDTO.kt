package com.ezen.lolketing.model

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
    val name : String,
    val group : String,
    val documentId: String
)