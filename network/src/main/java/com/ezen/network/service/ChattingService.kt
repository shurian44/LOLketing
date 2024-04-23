package com.ezen.network.service

import com.ezen.network.model.ChattingListParam
import com.ezen.network.model.ChattingRoomInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChattingService {
    @POST("/chatting/select/chattingList")
    suspend fun fetchChattingList(@Body item: ChattingListParam): Response<List<ChattingRoomInfo>>
}