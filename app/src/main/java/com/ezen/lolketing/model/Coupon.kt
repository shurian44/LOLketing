package com.ezen.lolketing.model

import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.getSimpleDateFormatMillSec

data class Coupon(
    var title: String? = null,
    var id: String? = null,
    var use: String? = Code.NOT_USE.code,
    var point: Int = 0,
    var couponNumber: String? = null,
    var limit: String? = null,
    var documentId: String? = "",
    var timestamp: Long? = System.currentTimeMillis()
) {
    fun mapperMyPageCouponInfo() = use == Code.NOT_USE.code && checkExpirationDate()

    private fun checkExpirationDate() =
        getSimpleDateFormatMillSec(limit ?: "2000-01-01 00:00", "yyyy-MM-dd HH:mm") >=
                System.currentTimeMillis()

    fun mapperCouponListInfo(documentId: String): CouponInfo? {
        return CouponInfo(
            title = title ?: return null,
            use = use ?: return null,
            point = point,
            limit = limit ?: return null,
            documentId = documentId
        )
    }

}

data class CouponInfo(
    val title: String,
    val use: String,
    val point: Int,
    val limit: String,
    val documentId: String
)