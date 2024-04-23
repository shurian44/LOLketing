package com.ezen.auth.client

import android.content.Context
import android.util.Log
import com.ezen.auth.exception.NaverException
import com.ezen.auth.model.JoinInfo
import com.ezen.auth.model.UserInfoType
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfile
import com.navercorp.nid.profile.data.NidProfileResponse
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NaverClient @Inject constructor() {
    companion object {
        private const val TAG = "NaverClient"
    }

    /** 로그인 **/
    suspend fun naverLogin(context: Context) = suspendCoroutine { cont ->
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                cont.resume(Unit)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e(TAG, "naverLogin onFailure [$httpStatus] - $message")
                cont.resumeWithException(NaverException())
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e(TAG, "naverLogin onError [$errorCode] - $message")
                cont.resumeWithException(NaverException())
            }
        }

        NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
    }

    /** 프로필 조회 **/
    suspend fun naverProfile() = suspendCoroutine { cont ->
        val callProfileApi = object : NidProfileCallback<NidProfileResponse> {

            override fun onSuccess(result: NidProfileResponse) {
                cont.resume(result.profile?.mapperToUserInfo())
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e(TAG, "naverProfile onFailure [$httpStatus] - $message")
                cont.resumeWithException(NaverException())
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e(TAG, "naverProfile onError [$errorCode] - $message")
                cont.resumeWithException(NaverException())
            }
        }

        NidOAuthLogin().callProfileApi(callProfileApi)
    }

    private fun NidProfile.mapperToUserInfo() : JoinInfo? {
        return JoinInfo(
            type = UserInfoType.Naver.type,
            id = email ?: "",
            password = "",
            passwordCheck = "",
            nickname = nickname ?: return null,
            gender = gender ?: "",
            birthday = birthday ?: "",
            birthYear = birthYear ?: "",
            mobile = mobile?.replace("-", "") ?: "",
            address = ""
        )
    }

    /** 연동 해제 **/
    suspend fun naverUnlink() = suspendCoroutine { cont ->
        val callback = object : OAuthLoginCallback {
            override fun onSuccess() {
                cont.resume("연동 해제 완료")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e(TAG, "naverUnlink onFailure [$httpStatus] - $message")
                cont.resumeWithException(NaverException())
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e(TAG, "naverUnlink onError [$errorCode] - $message")
                cont.resumeWithException(NaverException())
            }
        }

        NidOAuthLogin().callDeleteTokenApi(callback)
    }

    /** 로그 아웃 **/
    fun logout() {
        NaverIdLoginSDK.logout()
    }
}