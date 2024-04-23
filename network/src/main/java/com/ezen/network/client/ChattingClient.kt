package com.ezen.network.client

import com.ezen.network.model.ChattingListParam
import com.ezen.network.service.ChattingService
import com.ezen.network.util.result
import javax.inject.Inject

class ChattingClient @Inject constructor(
    private val service: ChattingService
) {
    suspend fun fetchChattingList(date: String) = runCatching {
        service.fetchChattingList(ChattingListParam(date)).result()
    }
}