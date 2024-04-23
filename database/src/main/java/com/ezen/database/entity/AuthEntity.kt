package com.ezen.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthEntity(
    @PrimaryKey(autoGenerate = true)
    val index: Int = 0,
    val id: Int,
    val email: String,
    val nickname: String
)