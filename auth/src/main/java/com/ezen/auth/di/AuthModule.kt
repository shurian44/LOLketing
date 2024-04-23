package com.ezen.auth.di

import com.ezen.auth.repository.AuthRepository
import com.ezen.auth.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindAuthRepository(
        repository: AuthRepositoryImpl
    ): AuthRepository

}