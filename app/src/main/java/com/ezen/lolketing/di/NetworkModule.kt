package com.ezen.lolketing.di

import com.ezen.lolketing.BuildConfig
import com.ezen.lolketing.network.AddressClient
import com.ezen.lolketing.network.NaverClient
import com.ezen.lolketing.network.service.AddressService
import com.ezen.lolketing.network.service.NaverService
import com.ezen.lolketing.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @AddressRetrofit
    @Provides
    @Singleton
    fun provideAddressRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.ADDRESS_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @NaverRetrofit
    @Provides
    @Singleton
    fun provideNaverRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.NAVER_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideAddressService(@AddressRetrofit retrofit: Retrofit) : AddressService =
        retrofit.create(AddressService::class.java)

    @Provides
    @Singleton
    fun provideAddressClient(addressService: AddressService) : AddressClient =
        AddressClient(addressService)

    @Provides
    @Singleton
    fun provideNaverService(@NaverRetrofit retrofit: Retrofit) : NaverService =
        retrofit.create(NaverService::class.java)

    @Provides
    @Singleton
    fun provideNaverClient(naverService: NaverService) : NaverClient =
        NaverClient(naverService)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AddressRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NaverRetrofit
}