package com.ezen.lolketing.network.service

import com.ezen.lolketing.BuildConfig
import com.ezen.lolketing.model.AddressResult
import retrofit2.http.*

interface AddressService {

    /** 주소 조회 **/
    @GET("addrlink/addrLinkApi.do")
    suspend fun fetchAddress(
        @Query("confmKey") confmKey : String? = BuildConfig.ADDRESS_API_KEY,
        @Query("currentPage") currentPage : Int? = 1,
        @Query("countPerPage") countPerPage : Int? = 10,
        @Query("keyword") keyword : String,
        @Query("resultType") resultType : String? = "json",
        @Query("firstSort") firstSort : String? = "road"
    ): AddressResult
}