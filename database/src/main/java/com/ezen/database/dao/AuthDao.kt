package com.ezen.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ezen.database.entity.AuthEntity

@Dao
interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInfo(authEntity: AuthEntity)

    @Query("SELECT COUNT(*) FROM AuthEntity")
    suspend fun isLogin(): Int

    @Query("SELECT email FROM AuthEntity LIMIT 1")
    suspend fun getUserEmail(): String

    @Query("SELECT id FROM AuthEntity LIMIT 1")
    suspend fun getUserId(): Int

    @Query("SELECT nickname FROM AuthEntity LIMIT 1")
    suspend fun getUserNickname(): String

    @Query("DELETE FROM AuthEntity")
    suspend fun logout()

}