package com.ezen.network.client

import com.ezen.network.model.StringIdParam
import com.ezen.network.model.ModifyInfo
import com.ezen.network.model.RouletteCouponUpdateItem
import com.ezen.network.model.UpdateCashItem
import com.ezen.network.model.UpdateCouponItem
import com.ezen.network.model.IntIdParam
import com.ezen.network.service.MainService
import com.ezen.network.util.result
import javax.inject.Inject

class MainClient @Inject constructor(
    private val service: MainService
) {
    suspend fun fetchMyInfo(id: Int) = runCatching {
        service.fetchMyInfo(IntIdParam(id)).result()
    }

    suspend fun fetchCashInfo(id: String) = runCatching {
        service.fetchCashInfo(StringIdParam(id)).result()
    }

    suspend fun updateCashCharging(item: UpdateCashItem) = runCatching {
        service.updateCashCharging(item).result()
    }

    suspend fun fetchCouponList(id: String) = runCatching {
        service.fetchCouponList(StringIdParam(id)).result()
    }

    suspend fun updateUsingCoupon(item: UpdateCouponItem) = runCatching {
        service.updateUsingCoupon(item).result()
    }

    suspend fun updateMyInfo(item: ModifyInfo) = runCatching {
        service.updateMyInfo(item).result()
    }

    suspend fun fetchNewUserCoupon(item: StringIdParam) = runCatching {
        service.fetchNewUserCoupon(item).result()
    }

    suspend fun insertNewUserCoupon(item: IntIdParam) = runCatching {
        service.insertNewUserCoupon(item).result()
    }

    suspend fun insertRouletteCoupon(item:RouletteCouponUpdateItem) = runCatching {
        service.insertRouletteCoupon(item).result()
    }

    suspend fun fetchRouletteCount(item: IntIdParam) = runCatching {
        service.fetchRouletteCount(item).result()
    }

    suspend fun fetchTicketHistory(item: IntIdParam) = runCatching {
        service.fetchTicketHistory(item).result()
    }

    suspend fun fetchGoodsHistory(item: IntIdParam) = runCatching {
        service.fetchGoodsHistory(item).result()
    }
}