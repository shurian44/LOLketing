package com.ezen.lolketing.repository

import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import javax.inject.Inject

class FindRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getFindUser(
        email : String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.getBasicSearchData(
            collection = Constants.USERS,
            field = Constants.ID,
            startDate = email,
            successListener = {
                if(it.documents.isEmpty()){
                    failureListener()
                } else {
                    successListener()
                }
            },
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}