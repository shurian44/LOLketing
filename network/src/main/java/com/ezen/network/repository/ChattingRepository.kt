package com.ezen.network.repository

import com.ezen.network.model.ChattingItem
import com.ezen.network.model.ChattingRoomInfo
import kotlinx.coroutines.flow.Flow

interface ChattingRepository {
    fun fetchChattingList(date: String): Flow<List<ChattingRoomInfo>>

    fun observeChatUpdates(info: ChattingRoomInfo): Flow<List<ChattingItem>>

    fun addChat(message: String, selectedTeam: String, info: ChattingRoomInfo): Flow<Unit>
}