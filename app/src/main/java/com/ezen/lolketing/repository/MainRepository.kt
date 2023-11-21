package com.ezen.lolketing.repository

import com.ezen.lolketing.network.FirebaseClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getUserInfo() = flow {
        client
            .getUserInfo()
            .onSuccess {
                emit(it.nickname.isNullOrEmpty().not())
            }
    }
}