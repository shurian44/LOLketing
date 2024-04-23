package com.ezen.network.client

import com.ezen.network.model.IntIdParam
import com.ezen.network.model.ProductPurchase
import com.ezen.network.model.RefundInfo
import com.ezen.network.model.TicketInfoParam
import com.ezen.network.model.ReservationTicketItem
import com.ezen.network.model.TicketIdParam
import com.ezen.network.service.PurchaseService
import com.ezen.network.util.result
import javax.inject.Inject

class PurchaseClient @Inject constructor(
    private val service: PurchaseService
) {
    suspend fun fetchGameList() = runCatching {
        service.fetchGameList().result()
    }

    suspend fun fetchReservedSeats(item: TicketInfoParam) = runCatching {
        service.fetchReservedSeats(item).result()
    }

    suspend fun reservationTicket(item: ReservationTicketItem) = runCatching {
        service.reservationTicket(item).result()
    }

    suspend fun fetchTicketInfo(item: TicketIdParam) = runCatching {
        service.fetchTicketInfo(item).result()
    }

    suspend fun refundTicket(item: RefundInfo) = runCatching {
        service.refundTicket(item).result()
    }

    suspend fun fetchGoodsItems() = runCatching {
        service.fetchGoodsItems().result()
    }

    suspend fun fetchGoodsItemDetail(item: IntIdParam) = runCatching {
        service.fetchGoodsItemDetail(item).result()
    }

    suspend fun insertProductPurchase(item: List<ProductPurchase>) = runCatching {
        service.insertProductPurchase(item).result()
    }

    suspend fun fetchPurchaseInfo(item: IntIdParam) = runCatching {
        service.fetchPurchaseInfo(item).result()
    }
}