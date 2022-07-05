package com.ezen.lolketing.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ezen.lolketing.database.dao.ShopDao
import com.ezen.lolketing.database.entity.ShopEntity

@Database(
    entities = [ShopEntity::class],
    version = 1
)
abstract class LolketingDatabase : RoomDatabase() {

    abstract fun shopDao() : ShopDao

    companion object {
        const val DATABASE_NAME = "lolketing.db"
    }

}