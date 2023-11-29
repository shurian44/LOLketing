package com.ezen.lolketing.model

data class LoginInfo(
    var id: String = "",
    var password: String = ""
)

data class JoinInfo(
    var id: String = "",
    var password: String = "",
    var passwordCheck: String = ""
)