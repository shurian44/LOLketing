package com.ezen.network.service

import com.ezen.network.model.Game
import com.ezen.network.model.Goods
import com.ezen.network.model.GoodsDetail
import com.ezen.network.model.IntIdParam
import com.ezen.network.model.ProductPurchase
import com.ezen.network.model.PurchaseInfo
import com.ezen.network.model.RefundInfo
import com.ezen.network.model.ReservationItem
import com.ezen.network.model.TicketInfoParam
import com.ezen.network.model.ReservationTicketItem
import com.ezen.network.model.TicketIdParam
import com.ezen.network.model.TicketInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PurchaseService {
    @POST("/purchase/select/game")
    suspend fun fetchGameList(): Response<List<Game>>

    @POST("/purchase/select/reservationInfo")
    suspend fun fetchReservedSeats(@Body item: TicketInfoParam): Response<ReservationItem>

    @POST("/purchase/insert/reservationTicket")
    suspend fun reservationTicket(@Body item: ReservationTicketItem): Response<List<Int>>

    @POST("/purchase/select/ticketInfo")
    suspend fun fetchTicketInfo(@Body item: TicketIdParam): Response<TicketInfo>

    @POST("/purchase/delete/tickets")
    suspend fun refundTicket(@Body item: RefundInfo): Response<String>

    @POST("/purchase/select/goodsItems")
    suspend fun fetchGoodsItems(): Response<List<Goods>>

    @POST("/purchase/select/goodsItemDetail")
    suspend fun fetchGoodsItemDetail(@Body item: IntIdParam): Response<GoodsDetail>

    @POST("/purchase/insert/items")
    suspend fun insertProductPurchase(@Body item: List<ProductPurchase>): Response<String>

    @POST("/purchase/select/purchaseInfo")
    suspend fun fetchPurchaseInfo(@Body item: IntIdParam): Response<PurchaseInfo>
}