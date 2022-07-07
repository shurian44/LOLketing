package com.ezen.lolketing.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ezen.lolketing.database.entity.ShopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShoppingBasket(shopEntity: ShopEntity) : Long

    @Query("SELECT * FROM ShopEntity")
    fun selectAllShoppingBasket() : Flow<List<ShopEntity>>

    @Query("SELECT * FROM ShopEntity WHERE id = :id")
    fun selectShoppingBasket(id: Long) : Flow<ShopEntity>

    @Query("SELECT * FROM ShopEntity WHERE id IN (:idList)")
    fun selectShoppingBasketList(idList: List<Long>) : Flow<List<ShopEntity>>

    @Query("UPDATE ShopEntity SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateBasketChecked(id: Long, isChecked: Boolean)

    @Query("DELETE FROM ShopEntity WHERE id IN (:idList)")
    suspend fun deleteBasketItems(idList: List<Long>)

    @Query("SELECT count(*) FROM ShopEntity")
    fun selectBasketCount() : Flow<Long>

}
