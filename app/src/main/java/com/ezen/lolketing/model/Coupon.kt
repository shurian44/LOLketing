package com.ezen.lolketing.model

import com.ezen.lolketing.util.Code

data class Coupon(
    var title : String ?= null,
    var id : String ?= null,
    var use : String ?= Code.USED.code,
    var point: Int = 0,
    var couponNumber : String?= null,
    var limit : String ?= null,
    var documentId : String ?= "",
    var timestamp: Long ?= System.currentTimeMillis()
) {

    fun mapperMyPageCouponInfo() : MyPageCouponInfo? {
        return MyPageCouponInfo(
            use = use ?: return null,
            limit = limit ?: return null,
            id = id ?: return null
        )
    }

}

data class MyPageCouponInfo(
    val id : String,
    val use : String,
    val limit : String,
)