package com.ezen.network.di

import com.ezen.network.repository.AddressRepository
import com.ezen.network.repository.AddressRepositoryImpl
import com.ezen.network.repository.BoardRepository
import com.ezen.network.repository.BoardRepositoryImpl
import com.ezen.network.repository.ChattingRepository
import com.ezen.network.repository.ChattingRepositoryImpl
import com.ezen.network.repository.MainRepository
import com.ezen.network.repository.MainRepositoryImpl
import com.ezen.network.repository.NewsRepository
import com.ezen.network.repository.NewsRepositoryImpl
import com.ezen.network.repository.PurchaseRepository
import com.ezen.network.repository.PurchaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindMainRepository(
        repository: MainRepositoryImpl
    ): MainRepository

    @Binds
    fun bindAddressRepository(
        repository: AddressRepositoryImpl
    ): AddressRepository

    @Binds
    fun bindPurchaseRepository(
        repository: PurchaseRepositoryImpl
    ): PurchaseRepository

    @Binds
    fun bindNewsRepository(
        repository: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    fun bindChattingRepository(
        repository: ChattingRepositoryImpl
    ): ChattingRepository

    @Binds
    fun bindBoardRepository(
        repository: BoardRepositoryImpl
    ): BoardRepository

}