package com.ezen.lolketing.model

data class PurchaseDTO(var id : String ?= null, var name : String ?= null, var image : String ?= null,
                       var status : String ?= null, var group : String ?= null, var price : Int = 0,
                       var amount : Int = 0, var timestamp : Long = 0, var information : String ?= null,
                       var message : String ?= null, var address : String ?= null)