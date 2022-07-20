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
        point = point,
        cache = cache
    )
    fun mapperShippingInfo(): ShippingInfo? {
        return ShippingInfo(
            id = id ?: return null,
            nickname = nickname ?: "",
            phone = phone ?: "",
            address = address ?: "",
            cache = cache ?: 0
        )
    }

    fun mapperMyPageInfo() : MyPageInfo? {
        return MyPageInfo(
            id = id ?: return null,
            nickname = nickname ?: return null,
            grade = grade ?: return null,
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
    val point : Long,
    val cache : Long
)