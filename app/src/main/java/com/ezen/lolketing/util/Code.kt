package com.ezen.lolketing.util

enum class Code(val code: String, val codeName: String) {
    NEW_USER_COUPON("COUPON001", "신규 가입 쿠폰"),
    ROULETTE_COUPON("COUPON002", "룰렛 쿠폰"),
    NOT_USE("STATUS001", "사용 안함"),
    USES("STATUS002", "사용함"),
    EXPIRY("STATUS003", "기간만료"),
    TICKETING_ON("STATUS004", "예매"),
    TICKETING_SCHEDULE_OPEN("STATUS005", "오픈\n예정"),
    TICKETING_SOLD_OUT("STATUS006", "매진"),
    TICKETING_FINISH("STATUS007", "종료"),
    BRONZE("USER001", "브론즈"),
    SILVER("USER002", "실버"),
    GOLD("USER003", "골드"),
    PLATINUM("USER004", "플래티넘"),
    MASTER("USER005", "마스터"),
    PURCHASE_TICKET("PURCHASE001", "ticket"),
}