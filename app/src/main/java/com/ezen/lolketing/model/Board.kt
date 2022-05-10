package com.ezen.lolketing.model

//@Parcelize
data class Board(
    val email : String?= null,
    val team : String?= null,
    val timestamp: Long?= null,
    val userId: String?= null,
    val subject: String?= null,
    val title : String?= null,
    val content: String?= null,
    val image : String?= null,
    val commentCounts : Int?= null,
    val like : Map<String, Boolean>?= null,
    val likeCounts : Int?= null,
    val views : Int?= null
)



// Parcelable
// todo kotlin-android-extensions 제거 후 진행 예정
