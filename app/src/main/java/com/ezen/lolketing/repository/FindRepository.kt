package com.ezen.lolketing.repository

import android.util.Log
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
            field = "id",
            startDate = email,
            successListener = {
                Log.e("+++++", "$email result : ${it.documents}")
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