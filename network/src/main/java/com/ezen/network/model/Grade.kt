package com.ezen.network.model

enum class Grade(
    val koreanName: String,
    val code: String,
    val maxPoint: Int,
    val image: String
) {
    BRONZE(
        "브론즈",
        "USER001",
        3_000,
        "https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/bronze.png?alt=media&token=0f60a4c2-a58f-4fd9-9546-b9d6f7bdfdd2"
    ),
    SILVER(
        "실버",
        "USER002",
        30_000,
        "https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/silver.png?alt=media&token=89072c89-30f4-49ee-9114-768450109c9e"
    ),
    GOLD(
        "골드",
        "USER003",
        300_000,
        "https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/gold.png?alt=media&token=dc94d6d4-633d-46e5-9833-a1195da7d4e0"
    ),
    PLATINUM(
        "플래티넘",
        "USER004",
        300_000,
        "https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/platinum.png?alt=media&token=55428f81-1907-414f-9eea-80cce2087d42"
    ),
    MASTER(
        "마스터",
        "USER005",
        300_000,
        "https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/master.png?alt=media&token=60ba025c-4357-4017-912c-94788f3c4e7e"
    );

    companion object {
        fun getImage(code: String) =
            values().firstOrNull { it.code == code }?.image ?: BRONZE.image

        fun getMaxPoint(code: String) =
            values().firstOrNull { it.code == code }?.maxPoint ?: BRONZE.maxPoint

        fun getKoreanName(code: String) =
            values().firstOrNull { it.code == code }?.koreanName ?: BRONZE.koreanName

        fun getNextGrade(code: String, point: Int): String =
            if (code == PLATINUM.code || code == MASTER.code) {
                ""
            } else if (point < SILVER.maxPoint) {
                SILVER.code
            } else if (point < GOLD.maxPoint){
                GOLD.code
            } else {
                PLATINUM.code
            }

        fun getGrade(code: String) =
            values().firstOrNull { it.code == code } ?: BRONZE
    }
}