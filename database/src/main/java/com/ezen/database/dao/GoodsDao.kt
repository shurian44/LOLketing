package com.ezen.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ezen.database.entity.GoodsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoodsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInfo(goodsEntity: GoodsEntity)

    @Query("SELECT * FROM GoodsEntity")
    fun fetchCartList(): Flow<List<GoodsEntity>>

    @Query("SELECT Count(*) FROM GoodsEntity")
    fun fetchCartCount(): Flow<Int>

    @Query("UPDATE GoodsEntity SET isChecked = :isChecked WHERE `index` = :index")
    suspend fun updateCheckedStatus(index: Int, isChecked: Boolean)

    @Query("UPDATE GoodsEntity SET isChecked = :isChecked")
    suspend fun updateCheckedStatusAll(isChecked: Boolean)

    @Query("UPDATE GoodsEntity SET amount = :amount WHERE `index` = :index")
    suspend fun updateAmount(index: Int, amount: Int)

    @Delete
    suspend fun deleteItems(items: List<GoodsEntity>)

}