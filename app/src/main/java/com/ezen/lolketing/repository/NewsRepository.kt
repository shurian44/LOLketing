package com.ezen.lolketing.repository

import com.ezen.lolketing.model.NewsContents
import com.ezen.lolketing.network.NaverClient
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val client: NaverClient
) {

    /** 뉴스 정보 조회 **/
    suspend fun getNews(
        query: String,
        display: Int,
        start: Int,
        sort: String,
        successListener: (List<NewsContents>, Int) -> Unit,
        failureListener: () -> Unit
    ) {

        client.getNews(
            query = query,
            display = display,
            sort = sort,
            start = start
        ).onSuccess {
            successListener(it.mapper(), it.total)
        }.onFailure {
            failureListener()
        }

    }

}