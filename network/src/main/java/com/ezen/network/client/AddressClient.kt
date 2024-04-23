package com.ezen.network.client

import com.ezen.network.service.AddressService
import javax.inject.Inject

class AddressClient @Inject constructor(
    private val service: AddressService
) {

    /** 주소 조회 **/
    suspend fun fetchAddress(
        keyword : String,
        currentPage : Int?,
    ) = runCatching {
        service.fetchAddress(
            keyword = keyword,
            currentPage = currentPage
        )
    }

}