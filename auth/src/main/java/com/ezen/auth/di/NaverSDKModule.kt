package com.ezen.auth.di

import android.content.Context
import com.ezen.auth.BuildConfig
import com.navercorp.nid.NaverIdLoginSDK
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object NaverSDKModule {

    @Provides
    fun provideNaverSDKInitializer(@ApplicationContext context: Context): NaverSDKInitializer {
        return NaverSDKInitializer(context)
    }
}

class NaverSDKInitializer @Inject constructor(private val context: Context) {

    fun initializeNaverSDK() {
        NaverIdLoginSDK.initialize(
            context = context,
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET,
            clientName = "롤켓팅"
        )
    }
}
