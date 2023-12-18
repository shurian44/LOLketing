package com.ezen.lolketing.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ezen.lolketing.model.PurchaseInfo
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    indices = [Index(
        value = ["group", "name", "price",
            "images", "count", "timestamp", "documentId", "isChecked"], unique = true
    )]
)
data class ShopEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "group") val group: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "price") val price: Long = 0,
    @ColumnInfo(name = "images") val image: String = "",
    @ColumnInfo(name = "count") val count: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long = 0,
    @ColumnInfo(name = "documentId") val documentId: String = "",
    @ColumnInfo(name = "isChecked") var isChecked: Boolean = true
) : Parcelable {
    fun mapper() = PurchaseInfo(
        databaseId = id,
        name = name,
        image = image,
        group = group,
        price = price,
        amount = count,
        timestamp = System.currentTimeMillis(),
        information = documentId,
        message = "",
        address = "",
        status = "",
        documentList = listOf()
    )
}