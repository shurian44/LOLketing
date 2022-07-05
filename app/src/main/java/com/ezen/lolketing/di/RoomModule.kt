package com.ezen.lolketing.di

import android.content.Context
import androidx.room.Room
import com.ezen.lolketing.database.LolketingDatabase
import com.ezen.lolketing.database.dao.ShopDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ) : LolketingDatabase =
        Room.databaseBuilder(context, LolketingDatabase::class.java, LolketingDatabase.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideAddressDao(
        database: LolketingDatabase
    ) : ShopDao =
        database.shopDao()

}