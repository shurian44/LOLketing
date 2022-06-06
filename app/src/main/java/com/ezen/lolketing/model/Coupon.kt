package com.ezen.lolketing.model

data class Coupon(
    var title : String ?= null,
    var id : String ?= null,
    var use : String ?= "사용 안함",
    var point: Int = 0,
    var couponNumber : String?= null,
    var limit : String ?= null,
    var documentId : String?= null
) {
    companion object {
        const val noneUsed = "사용 안함"
        const val alreadyUsed = "사용함"
    }
}