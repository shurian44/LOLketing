package com.ezen.lolketing.util

sealed class LoginException(
    override val message: String?
) : Throwable() {
    // 중복 가입
    object DuplicateJoin: LoginException("이미 가입된 아이디입니다.")
    // 회원 가입 실패
    object FailureJoin: LoginException("회원 가입에 실패하였습니다.")
    // 유저 삭제 오류
    object DeleteUserError: LoginException("계정 삭제에 실패하였습니다.")
    // 유저 정보 없음
    object EmptyUser: LoginException("유저 정보가 없습니다.")
    // 정보 없음
    object EmptyInfo: LoginException("정보가 없습니다.")
}