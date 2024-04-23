package com.ezen.auth.client

import com.ezen.auth.model.IdParam
import com.ezen.auth.model.LoginInfo
import com.ezen.auth.model.SocialLoginInfo
import com.ezen.auth.model.JoinInfo
import com.ezen.auth.service.AuthService
import com.ezen.network.util.result
import javax.inject.Inject

class AuthClient @Inject constructor(
    private val service: AuthService
) {

    suspend fun join(item: JoinInfo) = runCatching {
        service.join(item).result()
    }

    suspend fun emailLogin(item: LoginInfo) = runCatching {
        service.emailLogin(item).result()
    }

    suspend fun socialLogin(item: SocialLoginInfo) = runCatching {
        service.socialLogin(item).result()
    }

    suspend fun withdrawal(id: String) = runCatching {
        service.withdrawal(IdParam(id)).result()
    }

}