package com.ezen.auth.di

import android.content.Context
import com.ezen.auth.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object KakaoSdkModule {

    @Provides
    fun provideKakaoSdk(@ApplicationContext context: Context): KakaoSdkInitializer {
        return KakaoSdkInitializer(context)
    }
}

class KakaoSdkInitializer @Inject constructor(private val context: Context) {

    fun initializeKakaoSDK() {
        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}