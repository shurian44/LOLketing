package com.ezen.lolketing.network

import com.ezen.lolketing.model.AddressResult
import com.ezen.lolketing.network.service.AddressService
import retrofit2.Response
import javax.inject.Inject

class AddressClient @Inject constructor(
    private val addressService: AddressService
) {

    suspend fun getAddress(
        keyword : String,
        currentPage : Int?
    ) : Response<AddressResult> =
        addressService.getAddress(
            keyword = keyword,
            currentPage = currentPage
        )

}