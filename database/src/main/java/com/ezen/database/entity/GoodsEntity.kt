package com.ezen.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat


@Parcelize
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
): Parcelable {
    fun getPriceFormat() = DecimalFormat("###,###").format(price * amount).plus("원")

    fun getCategoryFormat() = "[${category}]"

}

@Parcelize
data class CartItem(
    val list: List<GoodsEntity>,
    val totalPrice: Long,
    val isAllSelect: Boolean
): Parcelable {
    fun getTotalPriceFormat() = DecimalFormat("###,###").format(totalPrice).plus("원")

    companion object {
        fun create() = CartItem(
            list = listOf(),
            totalPrice = 0,
            isAllSelect = false
        )
    }
}