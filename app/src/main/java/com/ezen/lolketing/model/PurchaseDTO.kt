package com.ezen.lolketing.model

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
    var documentList : List<String>? = null
) {
    fun ticketMapper() = TicketInfo(
        amount = amount,
        image = image?: "",
        gameTitle = name?: "",
        information = information ?: "",
        timestamp = timestamp,
        documentList = documentList ?: listOf()
    )

    companion object {
        const val USABLE = "usable"
    }
}

data class TicketInfo(
    val amount : Int,
    val image: String,
    val gameTitle: String,
    val information: String,
    val timestamp: Long,
    val documentList: List<String>
)