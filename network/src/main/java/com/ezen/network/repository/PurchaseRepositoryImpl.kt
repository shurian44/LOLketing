package com.ezen.network.repository

import com.ezen.database.repository.DatabaseRepository
import com.ezen.database.entity.GoodsEntity
import com.ezen.network.client.MainClient
import com.ezen.network.client.PurchaseClient
import com.ezen.network.model.IntIdParam
import com.ezen.network.model.ProductPurchase
import com.ezen.network.model.RefundInfo
import com.ezen.network.model.ReservationTicketItem
import com.ezen.network.model.TicketIdParam
import com.ezen.network.model.TicketInfoParam
import com.ezen.network.model.UpdateCashItem
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val client: PurchaseClient,
    private val mailClient: MainClient,
    private val databaseRepository: DatabaseRepository
): PurchaseRepository{
    override fun fetchGameList() = flow {
        client
            .fetchGameList()
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun fetchReservedSeats(gameId: Int) = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) {
            throw Exception("유저 정보가 없습니다.")
        }

        client
            .fetchReservedSeats(
                TicketInfoParam(
                    gameId = gameId,
                    userId = userId
                )
            )
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun reservationTicket(item: ReservationTicketItem) = flow {
        item.checkValidation()
        client
            .reservationTicket(item)
            .onSuccess { ids -> emit(ids.map { "$it" }.reduce { acc, s -> "$acc,$s" }) }
            .onFailure { throw it }
    }

    override fun fetchTicketInfo(item: TicketIdParam) = flow {
        client
            .fetchTicketInfo(item)
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun refundTicket(reservationIds: List<Int>) = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) {
            throw Exception("유저 정보가 없습니다.")
        }

        client
            .refundTicket(
                RefundInfo(
                    userId = userId,
                    reservationIds = reservationIds
                )
            )
            .onSuccess { emit(Unit) }
            .onFailure { throw it }
    }

    override fun cashChargingAndReservationInfo(cash: Int, gameId: Int) = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) {
            throw Exception("유저 정보가 없습니다.")
        }

        mailClient
            .updateCashCharging(
                UpdateCashItem(
                    id = userId,
                    cash = cash
                )
            )
            .onFailure { throw it }

        client
            .fetchReservedSeats(
                TicketInfoParam(
                    gameId = gameId,
                    userId = userId
                )
            )
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun cashCharging(cash: Int) = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) {
            throw Exception("유저 정보가 없습니다.")
        }

        mailClient
            .updateCashCharging(
                UpdateCashItem(
                    id = userId,
                    cash = cash
                )
            )
            .onSuccess { emit(it.cash) }
            .onFailure { throw it }
    }

    override fun fetchGoodsItems() = flow {
        client
            .fetchGoodsItems()
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun fetchGoodsItemDetail(goodsId: Int) = flow {
        client
            .fetchGoodsItemDetail(
                IntIdParam(goodsId)
            )
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun insertProductPurchase(items: List<GoodsEntity>) = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) {
            throw Exception("유저 정보가 없습니다.")
        }

        client
            .insertProductPurchase(
                items.map {
                    ProductPurchase(
                        userId = userId,
                        goodsId = it.goodsId,
                        amount = it.amount,
                        productsPrice = it.price * it.amount
                    )
                }
            )
            .onSuccess {
                emit(it)
                databaseRepository.deleteItems(items)
            }
            .onFailure { throw it }
    }

    override fun fetchPurchaseInfo() = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) {
            throw Exception("유저 정보가 없습니다.")
        }

        client
            .fetchPurchaseInfo(IntIdParam(userId))
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }
}