package com.ezen.auth.repository

import android.content.Context
import com.ezen.auth.model.LoginInfo
import com.ezen.auth.model.JoinInfo
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun join(joinInfo: JoinInfo): Flow<String>

    fun emailLogin(loginInfo: LoginInfo): Flow<Unit>

    fun naverLogin(context: Context): Flow<JoinInfo?>

    fun kakaoLogin(context: Context): Flow<JoinInfo?>

    suspend fun logout(): Result<Unit>

    suspend fun withdrawal(): Result<Unit>

    fun isLogin(): Flow<Boolean>

}