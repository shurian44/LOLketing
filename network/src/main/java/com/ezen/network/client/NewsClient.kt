package com.ezen.network.client

import com.ezen.network.service.NewsService
import javax.inject.Inject

class NewsClient @Inject constructor(
    private val service: NewsService
) {
    suspend fun fetchNews(
        query: String,
        display: Int,
        start: Int,
        sort: String
    ) = runCatching {
        service.fetchNews(
            query = query,
            display = display,
            start = start,
            sort = sort
        )
    }
}