package com.ezen.lolketing.repository

import com.ezen.lolketing.model.SearchResult
import com.ezen.lolketing.network.AddressClient
import java.lang.Exception
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val client : AddressClient
) {

    suspend fun selectAddress(
        keyword : String,
        currentPage: Int,
        successListener: (SearchResult) -> Unit,
        failureListener: () -> Unit
    )  =
        try {
            client.getAddress(
                keyword = keyword,
                currentPage = currentPage,
                successListener = {
                    successListener(it.results.toSearchResult())
                },
                failureListener = failureListener
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

}