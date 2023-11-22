package com.ezen.lolketing.util

sealed class LoginException : Throwable() {
    // 중복 가입
    object DuplicateJoin: LoginException()
    // 회원 가입 실패
    object FailureJoin: LoginException()
    // 유저 삭제 오류
    object DeleteUserError: LoginException()
    // 유저 정보 없음
    object EmptyUser: LoginException()
    // 정보 없음
    object EmptyInfo: LoginException()
}