package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getCurrentUser() = client.getCurrentUser()

    suspend fun getUserInfo(
        collectionPath : String,
        documentPath: String,
    ) : Users? = try {
        client.getUserInfo(collectionPath, documentPath)?.toObject(Users::class.java)
    } catch (e : Exception){
        null
    }

}