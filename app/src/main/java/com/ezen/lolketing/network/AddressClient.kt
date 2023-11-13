package com.ezen.lolketing.network

import com.ezen.lolketing.network.service.AddressService
import javax.inject.Inject

class AddressClient @Inject constructor(
    private val addressService: AddressService
) {

    /** 주소 조회 **/
    suspend fun fetchAddress(
        keyword : String,
        currentPage : Int?,
    ) = runCatching {
        addressService.fetchAddress(
            keyword = keyword,
            currentPage = currentPage
        )
    }

}