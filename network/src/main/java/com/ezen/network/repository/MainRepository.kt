package com.ezen.network.repository

import com.ezen.network.model.Coupon
import com.ezen.network.model.ModifyInfo
import com.ezen.network.model.MyCash
import com.ezen.network.model.MyInfo
import com.ezen.network.model.PurchaseHistoryItem
import com.ezen.network.model.RouletteCount
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun fetchMyInfo(): Flow<MyInfo>

    fun fetchModifyInfo(): Flow<ModifyInfo>
    
    fun fetchCashInfo(): Flow<MyCash>

    fun updateCashCharging(cash: Int): Flow<MyInfo>

    fun fetchCouponList(id: String): Flow<List<Coupon>>

    fun updateUsingCoupon(couponId: Int): Flow<MyInfo>

    fun updateMyInfo(item: ModifyInfo): Flow<Unit>

    fun fetchNewUserCoupon(): Flow<Boolean>

    fun insertNewUserCoupon(): Flow<Unit>

    suspend fun insertRouletteCoupon(rp: Int): Result<RouletteCount>

    fun fetchRouletteCount(): Flow<RouletteCount>

    fun fetchPurchaseHistoryInfo(): Flow<PurchaseHistoryItem>

}