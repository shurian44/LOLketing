package com.ezen.network.di

import com.ezen.network.BuildConfig
import com.ezen.network.client.AddressClient
import com.ezen.network.client.BoardClient
import com.ezen.network.client.ChattingClient
import com.ezen.network.client.MainClient
import com.ezen.network.client.NewsClient
import com.ezen.network.client.PurchaseClient
import com.ezen.network.service.AddressService
import com.ezen.network.service.BoardService
import com.ezen.network.service.ChattingService
import com.ezen.network.service.MainService
import com.ezen.network.service.NewsService
import com.ezen.network.service.PurchaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideGsonConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    @Named("main")
    fun provideMainRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://port-0-mj-api-e9btb72blgnd5rgr.sel3.cloudtype.app/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideMainService(
        @Named("main") retrofit: Retrofit
    ): MainService =
        retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideMainClient(
        service: MainService,
    ): MainClient = MainClient(service)

    @Provides
    @Singleton
    fun providePurchaseService(
        @Named("main") retrofit: Retrofit
    ): PurchaseService =
        retrofit.create(PurchaseService::class.java)

    @Provides
    @Singleton
    fun providePurchaseClient(
        service: PurchaseService,
    ): PurchaseClient = PurchaseClient(service)

    @Provides
    @Singleton
    fun provideChattingService(
        @Named("main") retrofit: Retrofit
    ): ChattingService =
        retrofit.create(ChattingService::class.java)

    @Provides
    @Singleton
    fun provideChattingClient(
        service: ChattingService,
    ): ChattingClient = ChattingClient(service)

    @Provides
    @Singleton
    fun provideBoardService(
        @Named("main") retrofit: Retrofit
    ): BoardService =
        retrofit.create(BoardService::class.java)

    @Provides
    @Singleton
    fun provideBoardClient(
        service: BoardService,
    ): BoardClient = BoardClient(service)

    @Provides
    @Singleton
    @Named("address")
    fun provideAddressRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://www.juso.go.kr/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideAddressService(@Named("address") retrofit: Retrofit) : AddressService =
        retrofit.create(AddressService::class.java)

    @Provides
    @Singleton
    fun provideAddressClient(addressService: AddressService) : AddressClient =
        AddressClient(addressService)

    @Provides
    @Singleton
    @Named("news")
    fun provideNewsRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    @Provides
    @Singleton
    fun provideNewsService(@Named("news") retrofit: Retrofit) : NewsService =
        retrofit.create(NewsService::class.java)

    @Provides
    @Singleton
    fun provideNewsClient(newsService: NewsService) : NewsClient =
        NewsClient(newsService)
}