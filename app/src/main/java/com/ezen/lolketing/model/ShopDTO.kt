package com.ezen.lolketing.model

import com.ezen.lolketing.database.entity.ShopEntity
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.priceFormat

data class ShopDTO(
    var images: List<String>? = null,
    var price: Long? = 0,
    var name: String? = null,
    var group: String? = null
) {
    fun mapper(documentId: String): ShopListItem? {
        return ShopListItem(
            image = images?.get(0) ?: return null,
            price = price ?: return null,
            name = name ?: return null,
            group = group ?: return null,
            documentId = documentId
        )
    }

    fun itemMapper(): ShopItem? {
        return ShopItem(
            images = images ?: return null,
            price = price ?: return null,
            amount = 1,
            name = name ?: return null,
            group = group ?: return null
        )
    }

    fun purchaseMapper(
        amount: Int,
        documentId: String,
    ): PurchaseInfo? {
        return PurchaseInfo(
            name = name ?: return null,
            image = images?.getOrNull(0) ?: return null,
            group = group ?: return null,
            price = price ?: return null,
            amount = amount,
            timestamp = System.currentTimeMillis(),
            information = documentId,
            message = "",
            address = "",
            status = "",
            documentList = listOf()
        )
    }
}

data class ShopListItem(
    val image: String,
    val price: Long,
    val name: String,
    val group: String,
    var documentId: String = ""
) {
    fun getCategory() = "[${findCodeName(group)}]"

    fun getPriceFormat() = price.priceFormat()
}

data class ShopItem(
    val images: List<String>,
    val price: Long,
    var amount: Int,
    val name: String,
    val group: String
) {
    fun getCategory() = "[${findCodeName(group)}]"

    fun getPriceFormat() = (price * amount).priceFormat()

    fun mapper(
        documentId: String
    ) = ShopEntity(
        group = group,
        name = name,
        price = price,
        image = images[0],
        count = amount,
        documentId = documentId,
        timestamp = System.currentTimeMillis()
    )

    companion object {
        fun create() = ShopItem(
            images = listOf(),
            price = 0,
            amount = 1,
            name = "",
            group = ""
        )
    }
}
