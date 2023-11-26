package com.ezen.lolketing.repository

import com.ezen.lolketing.network.NaverClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val client: NaverClient
) {

    /** 뉴스 정보 조회 **/
    fun fetchNews(
        query: String = "LCK",
        display: Int,
        start: Int,
        sort: String = "sim"
    ) = flow {
        emit(
            client
                .getNews(
                    query = query,
                    display = display,
                    sort = sort,
                    start = start
                )
                .getOrThrow()
        )
    }
}