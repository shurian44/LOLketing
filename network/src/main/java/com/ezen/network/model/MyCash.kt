package com.ezen.network.model

data class MyCash(
    val cash: Int
)

data class UpdateCashItem(
    val id: Int,
    val cash: Int
)