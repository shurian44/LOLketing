package com.ezen.network.util

import com.google.gson.Gson
import retrofit2.Response

fun <T> Response<T>.result(): T {
    val body = body()
    val errorBody = errorBody()
    return when {
        isSuccessful && body != null -> {
            body
        }
        errorBody != null -> {
            val result = Gson().fromJson(errorBody.string(), NetworkErrorResult::class.java)
            throw NetworkException(
                code = code(),
                message = result.detail
            )
        }
        else -> throw NetworkException(
            code = code(),
            message = "데이터가 없습니다."
        )
    }
}