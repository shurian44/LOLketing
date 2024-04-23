package com.ezen.network.repository

import com.ezen.network.model.NewsResult
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun fetchNews(
        display: Int,
        start: Int,
    ): Flow<NewsResult>
}