package com.ezen.lolketing.util

enum class Code(val code: String, val codeName: String) {
    NEW_USER_COUPON("COUPON001", "신규 가입 쿠폰"),
    ROULETTE_COUPON("COUPON002", "룰렛 쿠폰"),
    NOT_USE("STATUS001", "사용 안함"),
    USED("STATUS002", "사용함"),
    EXPIRY("STATUS003", "기간만료"),
    TICKETING_ON("STATUS004", "예매"),
    TICKETING_SCHEDULE_OPEN("STATUS005", "오픈\n예정"),
    TICKETING_SOLD_OUT("STATUS006", "매진"),
    TICKETING_FINISH("STATUS007", "종료"),
    FREE_BOARD("CATEGORY001", "자유 게시판"),
    QUESTION_BOARD("CATEGORY002", "질문 게시판"),
    GAME_BOARD("CATEGORY003", "게임 게시판"),
    REPORT_PROMOTION("REPORT001", "부적절한 홍보게시물"),
    REPORT_OBSCENITY("REPORT002", "음란성 또는 청소년에게 부적합한 내용"),
    REPORT_COPYRIGHT("REPORT003", "명예훼손/사생활 침해 및 저작권침해"),
    REPORT_ETC("REPORT004", "기타"),
    SHOP_ALL("SHOP001", "All"),
    STATUE("SHOP002", "스태츄"),
    FIGURE("SHOP003", "피규어"),
    ACCESSORY("SHOP04", "액세서리"),
    DOLL("SHOP005", "인형"),
    T_SHIRT("SHOP006", "티셔츠"),
    JACKET("SHOP007", "후드와 자켓"),
    PAJAMAS("SHOP008", "잠옷"),
    ART("SHOP009", "서적/아트"),
    PURCHASE_TICKET("PURCHASE001", "경기 티켓"),
    BOARD_GAME("SHOP010", "보드게임"),
    UNKNOWN("UNKNOWN", ""),
}

fun findCode(codeName: String) =
    Code.values().find { it.codeName == codeName }?.code ?: Code.UNKNOWN.code

fun findCodeName(code: String) =
    Code.values().find { it.code == code }?.codeName ?: Code.UNKNOWN.codeName
