package com.ezen.lolketing.model

import com.ezen.lolketing.util.Grade
import com.ezen.lolketing.util.getGrade
import com.ezen.lolketing.util.getGradeImageRes
import com.ezen.lolketing.util.getGradeName
import com.ezen.lolketing.util.priceFormat
import kotlin.math.min

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
    fun mapperCacheModify(): CacheModifyUser? {
        return CacheModifyUser(
            grade = grade ?: return null,
            point = point ?: return null,
            myCache = cache ?: return null
        )
    }
    fun mapperShoppingInfo(): ShoppingInfo? {
        return ShoppingInfo(
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
            grade = getGradeName(grade ?: Grade.BRONZE.gradeCode),
            point = point ?: return null,
            cache = cache ?: return null,
        )
    }
}

data class UserInfo(
    var nickname : String ?= null,
    var phone : String ?= null,
    var address : String ?= null
)

data class CacheModifyUser(
    val grade: String = Grade.BRONZE.gradeCode,
    val point: Long = 0,
    val myCache: Long = 0,
    val chargingCache: Long = 0
) {
    fun getChargingCacheFormat() = chargingCache.priceFormat()

    fun getMyCacheFormat() = myCache.priceFormat()

    fun updateInfo(): CacheModifyUser {
        var addPoint = 0
        addPoint += if(myCache + chargingCache > 100_000_000){
            (100_000_000 - myCache.toInt()) / 10
        }else{
            (chargingCache / 10).toInt()
        }

        val newPoint = min(point + addPoint, 100_000_000)

        return this.copy(
            myCache = min(myCache + chargingCache, 100_000_000),
            point = newPoint,
            grade = getGrade(newPoint)
        )
    }
}

data class ShoppingInfo(
    val id: String,
    var nickname: String,
    var phone: String,
    var address: String,
    val cache: Long
) {
    fun getCacheFormat() = cache.priceFormat()

    companion object {
        fun create() = ShoppingInfo(
            id = "",
            nickname = "",
            phone = "",
            address = "",
            cache = 0
        )
    }
}

data class MyPageInfo(
    val id : String,
    val nickname : String,
    val grade : String,
    val point : Long,
    val cache : Long,
    val couponInfo: String = ""
) {
    fun getGradeImage() = getGradeImageRes(grade)

    fun getMyCache() = cache.priceFormat()

    companion object {
        fun create() = MyPageInfo(
            id = "",
            nickname = "",
            grade = Grade.BRONZE.gradeName,
            point = 0,
            cache = 0,
            couponInfo = "0 / 0"
        )
    }
}