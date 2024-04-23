package com.ezen.network.repository

import com.ezen.database.entity.GoodsEntity
import com.ezen.network.model.Game
import com.ezen.network.model.Goods
import com.ezen.network.model.GoodsDetail
import com.ezen.network.model.PurchaseInfo
import com.ezen.network.model.ReservationTicketItem
import com.ezen.network.model.ReservationItem
import com.ezen.network.model.TicketIdParam
import com.ezen.network.model.TicketInfo
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {
    fun fetchGameList(): Flow<List<Game>>

    fun fetchReservedSeats(gameId: Int): Flow<ReservationItem>

    fun reservationTicket(item: ReservationTicketItem): Flow<String>

    fun fetchTicketInfo(item: TicketIdParam): Flow<TicketInfo>

    fun refundTicket(reservationIds: List<Int>): Flow<Unit>

    fun cashChargingAndReservationInfo(cash: Int, gameId: Int): Flow<ReservationItem>

    fun cashCharging(cash: Int): Flow<Int>

    fun fetchGoodsItems(): Flow<List<Goods>>

    fun fetchGoodsItemDetail(goodsId: Int): Flow<GoodsDetail>

    fun insertProductPurchase(items: List<GoodsEntity>): Flow<String>

    fun fetchPurchaseInfo(): Flow<PurchaseInfo>

}