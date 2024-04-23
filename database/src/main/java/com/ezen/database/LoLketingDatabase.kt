package com.ezen.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ezen.database.dao.AuthDao
import com.ezen.database.dao.GoodsDao
import com.ezen.database.entity.AuthEntity
import com.ezen.database.entity.GoodsEntity

@Database(
    entities = [
        AuthEntity::class,
        GoodsEntity::class
    ],
    version = 4,
    exportSchema = true
)
abstract class LoLketingDatabase: RoomDatabase() {
    abstract fun authDao(): AuthDao

    abstract fun goodsDao(): GoodsDao
}