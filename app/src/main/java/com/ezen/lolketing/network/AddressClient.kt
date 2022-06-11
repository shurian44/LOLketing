package com.ezen.lolketing.network

import android.util.Log
import com.ezen.lolketing.model.AddressResult
import com.ezen.lolketing.network.service.AddressService
import retrofit2.Response
import javax.inject.Inject

class AddressClient @Inject constructor(
    private val addressService: AddressService
) {

    suspend fun getAddress(
        keyword : String,
        currentPage : Int?,
        successListener: (AddressResult) -> Unit,
        failureListener: () -> Unit
    ) = try {
        val result = addressService.getAddress(
            keyword = keyword,
            currentPage = currentPage
        )

        if (result.isSuccessful) {
            result.body()?.let(successListener) ?: failureListener()
        } else {
            Log.e("AddressClient", "result fail\n${result.errorBody()}")
            failureListener()
        }

    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }


}