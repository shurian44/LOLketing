package com.ezen.lolketing.model

import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.timestampToString

data class PurchaseDTO(
    var id: String? = null,
    var name: String? = null,
    var image: String? = null,
    var status: String? = null,
    var group: String? = null,
    var price: Long = 0,
    var amount: Int = 0,
    var timestamp: Long = 0,
    var information: String? = null,
    var message: String? = null,
    var address: String? = null,
    var documentList: List<String>? = null
) {
    fun ticketMapper() = TicketInfo(
        amount = amount,
        image = image ?: "",
        gameTitle = name ?: "",
        information = information ?: "",
        timestamp = timestamp,
        documentList = documentList ?: listOf()
    )

    fun historyMapper(documentId: String): PurchaseHistory.History? {
        return PurchaseHistory.History(
            name = name ?: return null,
            image = image,
            group = group ?: return null,
            price = price,
            amount = amount,
            timestamp = timestamp,
            documentId = if (group == Code.PURCHASE_TICKET.code) {
                documentId
            } else {
                information ?: return null
            }
        )
    }

    companion object {
        const val USABLE = "usable"
    }
}

data class TicketInfo(
    val amount: Int,
    val image: String,
    val gameTitle: String,
    val information: String,
    val timestamp: Long,
    val documentList: List<String>
)

sealed class PurchaseHistory {
    data class PurchaseItem(
        val name: String,
        val image: String? = null,
        val group: String,
        val price: Long,
        val amount: Int,
        val documentId: String
    ) : PurchaseHistory()

    data class PurchaseDate(
        val date: String
    ) : PurchaseHistory()

    data class History(
        val name: String,
        val image: String? = null,
        val group: String,
        val price: Long,
        val amount: Int,
        val timestamp: Long,
        val documentId: String
    )
}

fun PurchaseHistory.History.mapper() = PurchaseHistory.PurchaseItem(
    name = name,
    image = image,
    group = group,
    price = price,
    amount = amount,
    documentId = documentId
)

fun List<PurchaseHistory.History>.mapper(): List<PurchaseHistory> {
    val result = mutableListOf<PurchaseHistory>()
    var date = ""

    forEach {
        val currentDate = it.timestamp.timestampToString("yyyy.MM.dd")
        if (date != currentDate) {
            date = currentDate
            result.add(PurchaseHistory.PurchaseDate(currentDate))
        }
        result.add(it.mapper())
    }

    return result
}