package com.ezen.database.client

import com.ezen.database.dao.AuthDao
import com.ezen.database.dao.GoodsDao
import com.ezen.database.entity.AuthEntity
import com.ezen.database.entity.GoodsEntity
import javax.inject.Inject

class DatabaseClient @Inject constructor(
    private val authDao: AuthDao,
    private val goodsDao: GoodsDao
) {

    suspend fun insertInfo(authEntity: AuthEntity) = runCatching {
        authDao.insertInfo(authEntity)
    }

    suspend fun isLogin() = runCatching {
        authDao.isLogin() > 0
    }

    suspend fun getUserEmail() = runCatching {
        authDao.getUserEmail()
    }

    suspend fun getUserId() = runCatching {
        authDao.getUserId()
    }

    suspend fun getUserNickname() = runCatching {
        authDao.getUserNickname()
    }

    suspend fun logout() = runCatching {
        authDao.logout()
    }

    suspend fun insertGoods(goodsEntity: GoodsEntity) = runCatching {
        goodsDao.insertInfo(goodsEntity)
    }

    fun fetchCartList() = runCatching {
        goodsDao.fetchCartList()
    }

    fun fetchCartCount() = runCatching {
        goodsDao.fetchCartCount()
    }

    suspend fun updateCheckedStatus(index: Int, isChecked: Boolean) = runCatching {
        goodsDao.updateCheckedStatus(index, isChecked)
    }

    suspend fun updateCheckedStatusAll(isChecked: Boolean) = runCatching {
        goodsDao.updateCheckedStatusAll(isChecked)
    }

    suspend fun updateAmount(index: Int, amount: Int) = runCatching {
        goodsDao.updateAmount(index, amount)
    }

    suspend fun deleteItems(items: List<GoodsEntity>) = runCatching {
        goodsDao.deleteItems(items)
    }

}