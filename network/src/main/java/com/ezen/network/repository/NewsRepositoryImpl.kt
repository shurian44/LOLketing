package com.ezen.network.repository

import com.ezen.network.client.NewsClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val client: NewsClient
): NewsRepository {
    /** 뉴스 정보 조회 **/
    override fun fetchNews(
        display: Int,
        start: Int,
    ) = flow {
        emit(
            client
                .fetchNews(
                    query = "LCK",
                    display = display,
                    sort = "date",
                    start = start
                )
                .getOrThrow()
        )
    }
}