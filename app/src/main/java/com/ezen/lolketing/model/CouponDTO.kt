package com.ezen.lolketing.model

data class CouponDTO(var title : String ?= null, var id : String ?= null,
                     var couponNumber : String ?= null, var expirationDate : String ?= null)