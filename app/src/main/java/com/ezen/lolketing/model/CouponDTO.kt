package com.ezen.lolketing.model

data class CouponDTO(var title : String ?= null, var id : String ?= null, var use : Boolean ?= false,
                     var couponNumber : String ?= null, var limit : String ?= null)