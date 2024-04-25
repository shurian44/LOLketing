package com.ezen.auth.model

data class LoginInfo(
    var id: String,
    var password: String
)

data class SocialLoginInfo(
    val type: String,
    val id: String
)