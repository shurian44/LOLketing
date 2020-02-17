package com.ezen.lolketing.model

data class ShopDTO(var images : ArrayList<String> ?= null, var price : Int ?= 0,
                   var name : String ?= null, var group : String ?= null)