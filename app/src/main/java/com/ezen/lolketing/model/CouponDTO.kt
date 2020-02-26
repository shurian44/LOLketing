package com.ezen.lolketing.model

data class CouponDTO(var title : String ?= null, var id : String ?= null, var use : String ?= "사용 안함",
                     var couponNumber : Map<String, String> = HashMap(), var limit : String ?= null)