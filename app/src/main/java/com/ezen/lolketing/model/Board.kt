package com.ezen.lolketing.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val views : Int?= null,
    var documentId : String?= null
) : Parcelable {
    @Parcelize
    data class Comment(
        val email : String?= null,
        val userId : String?= null,
        val timestamp : Long?= null,
        val comment : String?= null
    ) : Parcelable
}
