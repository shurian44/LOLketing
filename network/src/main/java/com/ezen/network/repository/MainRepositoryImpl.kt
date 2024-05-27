package com.ezen.network.repository

import com.ezen.network.client.MainClient
import com.ezen.network.model.StringIdParam
import com.ezen.network.model.ModifyInfo
import com.ezen.network.model.PurchaseHistoryInfo
import com.ezen.network.model.RouletteCouponUpdateItem
import com.ezen.network.model.UpdateCashItem
import com.ezen.network.model.UpdateCouponItem
import com.ezen.network.model.IntIdParam
import com.ezen.network.model.RouletteCount
import com.ezen.database.repository.DatabaseRepository
import com.ezen.network.model.PurchaseHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val client: MainClient,
    private val databaseRepository: DatabaseRepository
) : MainRepository {
    override fun fetchMyInfo() = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) throw Exception("유저 정보가 없습니다.")

        client
            .fetchMyInfo(userId)
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun fetchModifyInfo(): Flow<ModifyInfo> = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) throw Exception("유저 정보가 없습니다.")

        client
            .fetchMyInfo(userId)
            .onSuccess { emit(it.toModifyInfo()) }
            .onFailure { throw it }
    }

    override fun fetchCashInfo() = flow {
        val userId = databaseRepository.getUserEmail()
        if (userId.isEmpty()) throw Exception("유저 정보가 없습니다.")

        client
            .fetchCashInfo(userId)
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun updateCashCharging(cash: Int) = flow {
        if (cash < 1_000) throw Exception("최소 충전 금액은 1,000원 입니다. 충전 금액을 확인해주세요.")
        val userId = databaseRepository.getUserId()
        if (userId == 0) throw Exception("유저 정보가 없습니다.")

        client
            .updateCashCharging(
                UpdateCashItem(
                    id = userId,
                    cash = cash
                )
            )
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun fetchCouponList(id: String) = flow {
        client
            .fetchCouponList(id)
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun updateUsingCoupon(couponId: Int) = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) throw Exception("유저 정보가 없습니다.")

        client
            .updateUsingCoupon(UpdateCouponItem(id = userId, couponId = couponId))
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun updateMyInfo(item: ModifyInfo) = flow {
        item.checkValidation()
        client
            .updateMyInfo(item)
            .onSuccess { emit(Unit) }
            .onFailure { throw it }
    }

    override fun fetchNewUserCoupon() = flow {
        val userId = databaseRepository.getUserEmail()
        if (userId.isEmpty()) throw Exception("유저 정보가 없습니다.")

        client
            .fetchNewUserCoupon(StringIdParam(userId))
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun insertNewUserCoupon() = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) throw Exception("유저 정보가 없습니다.")

        client
            .insertNewUserCoupon(IntIdParam(userId))
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override suspend fun insertRouletteCoupon(rp: Int): Result<RouletteCount> {
        val userId = databaseRepository.getUserId()
        if (userId == 0) throw Exception("유저 정보가 없습니다.")

        return client
            .insertRouletteCoupon(RouletteCouponUpdateItem(userId, rp))
    }

    override fun fetchRouletteCount() = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) {
            throw Exception("유저 정보가 없습니다.")
        }


        client
            .fetchRouletteCount(IntIdParam(userId))
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun fetchPurchaseHistoryInfo() = flow {
        val userId = databaseRepository.getUserId()
        if (userId == 0) {
            throw Exception("유저 정보가 없습니다.")
        }

        val ticketList = client
            .fetchTicketHistory(IntIdParam(userId))
            .getOrThrow()

        val goodsList = client
            .fetchGoodsHistory(IntIdParam(userId))
            .getOrThrow()

        emit(
            PurchaseHistoryItem(
                ticketList = PurchaseHistoryInfo.ticketHistoryListMapper(ticketList),
                goodsList = PurchaseHistoryInfo.goodsHistoryListMapper(goodsList)
            )
        )
    }

}