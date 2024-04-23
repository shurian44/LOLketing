package com.ezen.auth.service

import com.ezen.auth.model.IdParam
import com.ezen.auth.model.LoginInfo
import com.ezen.auth.model.SocialLoginInfo
import com.ezen.auth.model.JoinInfo
import com.ezen.auth.model.TypeResult
import com.ezen.database.entity.AuthEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/user/join/email")
    suspend fun join(@Body item: JoinInfo): Response<Int>

    @POST("/user/login/email")
    suspend fun emailLogin(@Body item: LoginInfo): Response<AuthEntity>

    @POST("/user/login/social")
    suspend fun socialLogin(@Body item: SocialLoginInfo): Response<AuthEntity>

    @POST("/user/withdrawal")
    suspend fun withdrawal(@Body item: IdParam): Response<TypeResult>

}