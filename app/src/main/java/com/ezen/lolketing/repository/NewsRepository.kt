package com.ezen.lolketing.repository

import com.ezen.lolketing.model.NewsResult
import com.ezen.lolketing.network.NaverClient
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val client: NaverClient
) {

    suspend fun getNews(
        query: String,
        display: Int,
        start: Int,
        sort: String,
        successListener: (NewsResult) -> Unit,
        failureListener: () -> Unit
    ) {

        client.getNews(
            query = query,
            display = display,
            sort = sort,
            start = start,
            successListener = successListener,
            failureListener = failureListener
        )

    }

}