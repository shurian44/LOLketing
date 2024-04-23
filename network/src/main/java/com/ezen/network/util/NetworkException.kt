package com.ezen.network.util

data class NetworkErrorResult(
    val detail: String
)

data class NetworkException(
    override val message: String?,
    val code: Int
) : Throwable(message)