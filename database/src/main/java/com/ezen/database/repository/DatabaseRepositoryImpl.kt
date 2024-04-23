package com.ezen.database.repository

import com.ezen.database.client.DatabaseClient
import com.ezen.database.entity.AuthEntity
import com.ezen.database.entity.GoodsEntity
import com.ezen.database.repository.DatabaseRepository
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val client: DatabaseClient
) : DatabaseRepository {

    override suspend fun insertInfo(id: Int, email: String, nickname: String) =
        client.insertInfo(AuthEntity(id = id, email = email, nickname = nickname))

    override suspend fun isLogin(): Boolean =
        client.isLogin().getOrElse { false }

    override suspend fun getUserEmail(): String =
        client.getUserEmail().getOrElse { "" }

    override suspend fun getUserNickname(): String =
        client.getUserNickname().getOrElse { "" }

    override suspend fun getUserId(): Int =
        client.getUserId().getOrElse { 0 }

    override suspend fun logout() =
        client.logout()

    override suspend fun insertGoods(goodsEntity: GoodsEntity) =
        client.insertGoods(goodsEntity)

    override fun fetchCartList() = client
        .fetchCartList()
        .getOrThrow()

    override fun fetchCartCount() = client
        .fetchCartCount()
        .getOrElse { emptyFlow() }

    override suspend fun updateCheckedStatus(index: Int, isChecked: Boolean) =
        client.updateCheckedStatus(index, isChecked)

    override suspend fun updateCheckedStatusAll(isChecked: Boolean) =
        client.updateCheckedStatusAll(isChecked)

    override suspend fun updateAmount(index: Int, amount: Int) =
        client.updateAmount(index, amount)

    override suspend fun deleteItems(items: List<GoodsEntity>) =
        client.deleteItems(items)

}