package com.ezen.lolketing.model

data class Users(
    var id : String ?= null,
    var uid : String ?= null,
    var nickname : String ?= null,
    var phone : String ?= null,
    var address : String ?= null,
    var grade : String ?= null,
    var rouletteCount : Int ?= 0,
    var accPoint : Int ?= 0,
    var point : Int ?= 0,
    var cache : Long ?= 0
) {
    fun mapper() = CacheModifyUser(
        grade = grade,
        accPoint = accPoint,
        point = point,
        cache = cache
    )
}

data class CacheModifyUser(
    var grade: String?= null,
    var accPoint: Int?= 0,
    var point: Int?= 0,
    var cache: Long?= 0
)