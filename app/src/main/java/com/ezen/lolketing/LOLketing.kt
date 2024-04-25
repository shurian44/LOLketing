package com.ezen.lolketing

import android.app.Application
import com.ezen.auth.di.KakaoSdkInitializer
import com.ezen.auth.di.NaverSDKInitializer
import com.ezen.network.NetworkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LOLketing : Application() {
    @Inject
    lateinit var naverSdk: NaverSDKInitializer
    @Inject
    lateinit var kakaoSdk: KakaoSdkInitializer

    companion object {
        private lateinit var application: LOLketing
        fun getInstance() : LOLketing = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        NetworkManager().initFirebase(applicationContext)
        naverSdk.initializeNaverSDK()
        kakaoSdk.initializeKakaoSDK()
    }
}