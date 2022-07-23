package com.ezen.lolketing.network

import com.ezen.lolketing.model.AddressResult
import com.ezen.lolketing.network.service.AddressService
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

        successListener(result)
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}