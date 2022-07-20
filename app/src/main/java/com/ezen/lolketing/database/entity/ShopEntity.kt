package com.ezen.lolketing.database.entity

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(indices = [Index(value = ["group", "name", "price",
    "images", "count", "timestamp", "documentId", "isChecked"], unique = true)])
data class ShopEntity (
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    @ColumnInfo(name = "group") val group : String = "",
    @ColumnInfo(name = "name") val name : String = "",
    @ColumnInfo(name = "price") val price : Long = 0,
    @ColumnInfo(name = "images") val image : String = "",
    @ColumnInfo(name = "count") val  count : Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long = 0,
    @ColumnInfo(name = "documentId") val documentId: String = "",
    @ColumnInfo(name = "isChecked") var isChecked: Boolean = true
) : Parcelable {
    companion object NavigationType : NavType<ShopEntity>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ShopEntity? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): ShopEntity {
            return Gson().fromJson(value, ShopEntity::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: ShopEntity) {
            bundle.putParcelable(key, value)
        }
    }
}