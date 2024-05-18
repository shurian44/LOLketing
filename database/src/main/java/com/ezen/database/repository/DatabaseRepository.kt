package com.ezen.database.repository

import com.ezen.database.entity.CartItem
import com.ezen.database.entity.GoodsEntity
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    suspend fun insertInfo(id: Int, email: String, nickname: String): Result<Unit>

    suspend fun isLogin(): Boolean

    suspend fun getUserEmail(): String

    suspend fun getUserNickname(): String

    suspend fun getUserId(): Int

    suspend fun logout(): Result<Unit>

    suspend fun insertGoods(goodsEntity: GoodsEntity): Result<Unit>

    fun fetchCartList(): Flow<CartItem>

    fun fetchCartCount(): Flow<Int>

    suspend fun updateCheckedStatus(index: Int, isChecked: Boolean): Result<Unit>

    suspend fun updateCheckedStatusAll(isChecked: Boolean): Result<Unit>

    suspend fun updateAmount(index: Int, amount: Int): Result<Unit>

    suspend fun deleteItems(items: List<GoodsEntity>): Result<Unit>

}