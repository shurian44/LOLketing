package com.ezen.lolketing.util

enum class Code(val code: String, val codeName: String) {
    NEW_USER_COUPON("COUPON001", "신규 가입 쿠폰"),
    ROULETTE_COUPON("COUPON002", "룰렛 쿠폰"),
    NOT_USE("STATUS001", "사용 안함"),
    USES("STATUS002", "사용함"),
    EXPIRY("STATUS003", "기간만료")
}