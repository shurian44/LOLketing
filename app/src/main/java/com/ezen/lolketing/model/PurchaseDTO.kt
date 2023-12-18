package com.ezen.lolketing.model

import android.os.Parcelable
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.util.timestampToString
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

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
            image = image ?: return null,
            category = "[${findCodeName(group ?: return null)}]",
            price = price,
            amount = amount,
            date = timestamp.timestampToString("yyyy.MM.dd"),
            documentId = documentId
        )
    }

    companion object {
        const val USABLE = "usable"
    }
}

data class PurchaseInfo(
    var databaseId: Long = 0,
    var name: String,
    var image: String,
    var status: String,
    var group: String,
    var price: Long = 0,
    var amount: Int = 0,
    var timestamp: Long = 0,
    var information: String,
    var message: String,
    var address: String,
    var documentList: List<String>?,
    var isChecked: Boolean = false
) {
    fun mapper(info: ShoppingInfo): PurchaseDTO? {
        return PurchaseDTO(
            id = info.id,
            name = name.ifEmpty { return null },
            image = image.ifEmpty { return null },
            status = status,
            group = group.ifEmpty { return null },
            price = price,
            amount = amount,
            timestamp = System.currentTimeMillis(),
            information = information,
            message = message,
            address = info.address,
            documentList = documentList
        )
    }

    fun getCategory() = "[${findCodeName(group)}]"

    fun getAmountFormat() = "${amount}개"

    fun getPriceFormat() = price.priceFormat()

    companion object {
        fun create() = PurchaseInfo(
            name = "",
            image = "",
            status = "",
            group = "",
            price = 0,
            amount = 0,
            timestamp = 0,
            information = "",
            message = "",
            address = "",
            documentList = listOf()
        )
    }
}

data class TicketInfo(
    val amount: Int,
    val image: String,
    val gameTitle: String,
    val information: String,
    val timestamp: Long,
    val documentList: List<String>
) {
    fun getTime() = runCatching {
        information.split(",")[0].trim()
    }.getOrElse { "0" }

    fun getSeats() = runCatching {
        val list = information.split(",")
        list.subList(1, list.size).joinToString(", ") { it.trim() }
    }.getOrElse { "" }

    fun isNoRefund(): Boolean {
        val currentTime = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
        val ticketTime = timeFormat.parse(getTime())?.time ?: 0
        val fourHour = 1000L * 60 * 60 * 4

        return currentTime > ticketTime - fourHour
    }

    private fun isFeeWaiver() : Boolean {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val currentTime = System.currentTimeMillis()

        return dateFormat.format(timestamp) == dateFormat.format(currentTime)
    }

    fun isRefundPossible() = !isNoRefund()

    fun getRefund() = when {
        isNoRefund() -> 0L
        isFeeWaiver() -> 11_000L * amount
        else -> (11_000 * amount * 0.9).toLong()
    }

    fun getQrCodeName() = "ticket/${gameTitle}_${information}"
        .replace(",", "_")
        .replace(" ", "")

    companion object {
        fun create() = TicketInfo(
            amount = 0,
            image = "",
            gameTitle = "",
            information = "",
            timestamp = 0,
            documentList = listOf()
        )
    }
}

@Parcelize
data class TicketTemp(
    val amount: Int,
    val gameTitle: String,
    val time: String,
    val seats: String,
    val documentList: List<String>
) : Parcelable {

    fun validationCheck() = when {
        amount !in 1..2 -> throw Exception("인원수를 확인해주세요")
        gameTitle.isEmpty() || time.isEmpty() || seats.isEmpty() || documentList.isEmpty() ->
            throw Exception("오류 발생")
        else -> this
    }

    fun getPrice() = amount * 11_000L

    fun getPriceWon() = getPrice().priceFormat()

    fun qrCodeInfo() = "ticket/${gameTitle}_${time}_$seats"
        .replace(",", "_")
        .replace(" ", "")

    fun toPurchaseDTO(
        id: String,
        image: String
    ): PurchaseDTO {
        return PurchaseDTO(
            id = id,
            name = gameTitle,
            image = image,
            status = Code.NOT_USE.code,
            group = Code.PURCHASE_TICKET.code,
            price = getPrice(),
            amount = amount,
            timestamp = System.currentTimeMillis(),
            information = "$time, $seats",
            message = "",
            address = "",
            documentList = documentList
        )
    }

    companion object {
        fun create() = TicketTemp(
            amount = 0,
            gameTitle = "",
            time = "",
            seats = "",
            documentList = listOf()
        )
    }
}

sealed class PurchaseHistory(val type: Int) {
    data class PurchaseDate(
        val date: String
    ) : PurchaseHistory(DATE)

    data class History(
        val image: String,
        val name: String,
        val category: String,
        val amount: Int,
        val price: Long,
        val date: String,
        val documentId: String
    ): PurchaseHistory(HISTORY) {
        fun getPriceFormat() = price.priceFormat()

        fun getAmountFormat() = "${amount}개"
    }

    companion object {
        const val DATE = 1
        const val HISTORY = 2
    }
}

//fun PurchaseHistory.History.mapper() = PurchaseHistory.PurchaseItem(
//    name = name,
//    image = image,
//    group = group,
//    price = price,
//    amount = amount,
//    documentId = documentId
//)
//
//fun List<PurchaseHistory.History>.mapper(): List<PurchaseHistory> {
//    val result = mutableListOf<PurchaseHistory>()
//    var date = ""
//
//    forEach {
//        val currentDate = it.timestamp.timestampToString("yyyy.MM.dd")
//        if (date != currentDate) {
//            date = currentDate
//            result.add(PurchaseHistory.PurchaseDate(currentDate))
//        }
//        result.add(it.mapper())
//    }
//
//    return result
//}