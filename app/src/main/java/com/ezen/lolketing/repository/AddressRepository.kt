package com.ezen.lolketing.repository

import com.ezen.lolketing.model.SearchResult
import com.ezen.lolketing.network.AddressClient
import java.lang.Exception
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val client : AddressClient
) {

    suspend fun selectAddress(keyword : String, currentPage: Int) : SearchResult? =
        try {
            val result = client.getAddress(keyword, currentPage)
            if (result.isSuccessful) {
                result.body()?.results?.toSearchResult()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

}