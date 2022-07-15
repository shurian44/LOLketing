package com.ezen.lolketing.model

import com.ezen.lolketing.util.Code

data class Coupon(
    var title : String ?= null,
    var id : String ?= null,
    var use : String ?= Code.NOT_USE.code,
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

    fun mapperCouponListInfo(documentId: String) : CouponInfo? {
        return CouponInfo(
            title = title ?: return null,
            use = use ?: return null,
            point = point,
            limit = limit ?: return null,
            documentId = documentId
        )
    }

}

data class MyPageCouponInfo(
    val id : String,
    val use : String,
    val limit : String,
)

data class CouponInfo(
    val title : String,
    val use : String,
    val point: Int,
    val limit : String,
    val documentId : String
)