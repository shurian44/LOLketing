package com.ezen.lolketing.model

data class Users(
    var id : String ?= null,
    var uid : String ?= null,
    var nickname : String ?= null,
    var phone : String ?= null,
    var address : String ?= null,
    var grade : String ?= null,
    var rouletteCount : Int ?= 0,
    var accPoint : Long ?= 0,
    var point : Long ?= 0,
    var cache : Long ?= 0
) {
    fun mapper() = CacheModifyUser(
        grade = grade,
        accPoint = accPoint,
        point = point,
        cache = cache
    )
    fun mapperShippingInfo(): ShippingInfo? {
        return ShippingInfo(
            id = id ?: return null,
            nickname = nickname ?: return null,
            phone = phone ?: return null,
            address = address ?: return null,
            cache = cache ?: return null
        )
    }

    fun mapperMyPageInfo() : MyPageInfo? {
        return MyPageInfo(
            id = id ?: return null,
            nickname = nickname ?: return null,
            grade = grade ?: return null,
            accPoint = accPoint ?: return null,
            point = point ?: return null,
            cache = cache ?: return null
        )
    }
}

data class UserInfo(
    var nickname : String ?= null,
    var phone : String ?= null,
    var address : String ?= null
)

data class CacheModifyUser(
    var grade: String?= null,
    var accPoint: Long?= 0,
    var point: Long?= 0,
    var cache: Long?= 0
)

data class ShippingInfo(
    val id: String,
    var nickname: String,
    var phone: String,
    var address: String,
    val cache: Long
)

data class MyPageInfo(
    val id : String,
    val nickname : String,
    val grade : String,
    val accPoint : Long,
    val point : Long,
    val cache : Long
)