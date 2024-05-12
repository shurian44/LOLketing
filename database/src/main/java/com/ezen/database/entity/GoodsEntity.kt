package com.ezen.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DecimalFormat


@Entity
data class GoodsEntity (
    @PrimaryKey(autoGenerate = true)
    val index: Int = 0,
    val category: String,
    val name: String,
    val price: Int,
    val amount: Int,
    val isChecked: Boolean = true,
    val image: String,
    val goodsId: Int
) {
    fun getPriceFormat() = DecimalFormat("###,###").format(price).plus("원")
}