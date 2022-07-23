package com.ezen.lolketing.network

import com.ezen.lolketing.model.NewsResult
import com.ezen.lolketing.network.service.NaverService
import javax.inject.Inject

class NaverClient @Inject constructor(
    private val service: NaverService
) {

    suspend fun getNews(
        query: String,
        display: Int,
        start: Int,
        sort: String,
        successListener: (NewsResult) -> Unit,
        failureListener: () -> Unit
    ) = try {
        val result = service.getNews(
            query = query,
            display = display,
            start = start,
            sort = sort
        )

        successListener(result)
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}