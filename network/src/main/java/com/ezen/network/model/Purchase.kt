package com.ezen.network.model

import com.ezen.database.entity.CartItem
import com.ezen.database.entity.GoodsEntity
import com.ezen.network.util.priceFormat
import java.text.DecimalFormat

sealed class PurchaseHistoryInfo(val type: Int) {
    data class PurchaseHistoryDate(
        val date: String
    ): PurchaseHistoryInfo(DATE)

    data class PurchaseTicketHistory(
        val reservationIds: String,
        val seatNumbers: String,
        val date: String,
        val time: String,
        val leftTeam: String,
        val rightTeam: String
    ): PurchaseHistoryInfo(HISTORY)

    data class PurchaseGoodsHistory(
        val amount: Int,
        val category: String,
        val name: String,
        val price: Int,
        val image: String,
        val date: String
    ): PurchaseHistoryInfo(HISTORY) {
        fun getPriceFormat() = price.priceFormat()

        fun getAmountFormat() = "$amount 개"
    }

    companion object {
        fun ticketHistoryListMapper(list: List<PurchaseTicketHistory>): List<PurchaseHistoryInfo> {
            val newList = mutableListOf<PurchaseHistoryInfo>()
            list
                .groupBy { ticket -> ticket.date + " " + ticket.time }
                .mapKeys { (key, value) ->
                    newList.add(PurchaseHistoryDate(key))
                    newList.addAll(value)
                }
            return newList
        }

        fun goodsHistoryListMapper(list: List<PurchaseGoodsHistory>): List<PurchaseHistoryInfo> {
            val newList = mutableListOf<PurchaseHistoryInfo>()
            list
                .groupBy { ticket -> ticket.date }
                .mapKeys { (key, value) ->
                    newList.add(PurchaseHistoryDate(key))
                    newList.addAll(value)
                }
            return newList
        }

        const val DATE = 1
        const val HISTORY = 2
    }
}

data class PurchaseHistoryItem(
    val isTicket: Boolean = true,
    val ticketList: List<PurchaseHistoryInfo> = listOf(),
    val goodsList: List<PurchaseHistoryInfo> = listOf(),
)

data class Goods(
    val goodsId: Int,
    val category: String,
    val name: String,
    val price: Int,
    val url: String
) {
    fun getPriceFormat() = price.priceFormat()
}

data class GoodsDetail(
    val goodsId: Int,
    val category: String,
    val name: String,
    val price: Int,
    val imageList: List<String>,
    val amount: Int
) {
    fun toEntity() = GoodsEntity(
        index = 0,
        category = category,
        name = name,
        price = price,
        amount = amount,
        image = imageList.getOrElse(0, defaultValue = { "" }),
        goodsId = goodsId
    )

    fun toCartItem() = CartItem(
        list = listOf(toEntity()),
        totalPrice = price * amount.toLong(),
        isAllSelect = true
    )

    fun getPriceFormat() = (amount * price).priceFormat()

    companion object {
        fun init() = GoodsDetail(
            category = "",
            name = "",
            price = 0,
            imageList = listOf(),
            goodsId = 0,
            amount = 1
        )
    }
}

data class ProductPurchase(
    val userId: Int,
    val goodsId: Int,
    val amount: Int,
    val productsPrice: Int
)

data class PurchaseInfo(
    val nickname: String,
    val mobile: String,
    val address: String,
    val cash: Int
) {
    companion object {
        fun init() = PurchaseInfo(
            "",
            "",
            "",
            0
        )
    }

    fun checkValidation() = when {
        nickname.isEmpty() || mobile.isEmpty() || address.isEmpty() ->
            false
        else -> true
    }

    fun getFormatCash() = DecimalFormat("###,###").format(cash).plus("원")
}