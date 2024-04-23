package com.ezen.database.module

import android.app.Application
import androidx.room.Room
import com.ezen.database.LoLketingDatabase
import com.ezen.database.dao.AuthDao
import com.ezen.database.dao.GoodsDao
import com.ezen.database.repository.DatabaseRepository
import com.ezen.database.repository.DatabaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): LoLketingDatabase = Room
        .databaseBuilder(application, LoLketingDatabase::class.java, "lolketingXml.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideAuthDao(
        database: LoLketingDatabase
    ): AuthDao = database.authDao()

    @Provides
    @Singleton
    fun provideGoodsDao(
        database: LoLketingDatabase
    ): GoodsDao = database.goodsDao()

}

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseRepositoryModule {
    @Binds
    fun bindDatabaseRepository(
        databaseRepositoryImpl: DatabaseRepositoryImpl
    ): DatabaseRepository
}