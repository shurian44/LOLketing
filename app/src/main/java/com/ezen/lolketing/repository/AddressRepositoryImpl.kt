package com.ezen.lolketing.repository

import com.ezen.lolketing.network.AddressClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val client : AddressClient
): AddressRepository {

    /** 주소 조회 **/
    override fun fetchAddress(keyword: String, currentPage: Int) = flow {
        emit(
            client
                .fetchAddress(keyword, currentPage)
                .getOrThrow()
                .results
                .toSearchResult()
        )
    }

}