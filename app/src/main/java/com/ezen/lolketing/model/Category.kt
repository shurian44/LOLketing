package com.ezen.lolketing.model

import com.ezen.lolketing.util.findCode

data class Category (
    val data : String,
    var isSelect : Boolean = false
) {
    fun getCode() = findCode(data)
}