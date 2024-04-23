package com.ezen.network.service

import com.ezen.network.model.Coupon
import com.ezen.network.model.StringIdParam
import com.ezen.network.model.ModifyInfo
import com.ezen.network.model.MyCash
import com.ezen.network.model.MyInfo
import com.ezen.network.model.PurchaseHistoryInfo
import com.ezen.network.model.RouletteCount
import com.ezen.network.model.UpdateCashItem
import com.ezen.network.model.UpdateCouponItem
import com.ezen.network.model.RouletteCouponUpdateItem
import com.ezen.network.model.IntIdParam
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainService {
    @POST("/user/select/myInfo")
    suspend fun fetchMyInfo(@Body item: IntIdParam): Response<MyInfo>

    @POST("/user/select/cash")
    suspend fun fetchCashInfo(@Body item: StringIdParam): Response<MyCash>

    @POST("/user/update/charging")
    suspend fun updateCashCharging(@Body item: UpdateCashItem): Response<MyInfo>

    @POST("/user/select/couponList")
    suspend fun fetchCouponList(@Body item: StringIdParam): Response<List<Coupon>>

    @POST("/user/update/usingCoupon")
    suspend fun updateUsingCoupon(@Body item: UpdateCouponItem): Response<MyInfo>

    @POST("/user/update/myInfo")
    suspend fun updateMyInfo(@Body item: ModifyInfo): Response<Unit>

    @POST("/user/select/newUserCoupon")
    suspend fun fetchNewUserCoupon(@Body item: StringIdParam): Response<Boolean>

    @POST("/user/insert/newUserCoupon")
    suspend fun insertNewUserCoupon(@Body item: IntIdParam): Response<Unit>

    @POST("/user/insert/rouletteCoupon")
    suspend fun insertRouletteCoupon(@Body item: RouletteCouponUpdateItem): Response<RouletteCount>

    @POST("/user/select/roulette")
    suspend fun fetchRouletteCount(@Body item: IntIdParam): Response<RouletteCount>

    @POST("/purchase/select/ticketHistory")
    suspend fun fetchTicketHistory(@Body item: IntIdParam):
            Response<List<PurchaseHistoryInfo.PurchaseTicketHistory>>

    @POST("/purchase/select/GoodsHistory")
    suspend fun fetchGoodsHistory(@Body item: IntIdParam):
            Response<List<PurchaseHistoryInfo.PurchaseGoodsHistory>>

}