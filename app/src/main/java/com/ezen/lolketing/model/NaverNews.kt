package com.ezen.lolketing.model

import com.google.gson.annotations.SerializedName

data class NewsResult(
    val lastBuildDate : String, // 검색 결과를 생성한 시간이다.
    val total : Int, // 검색 결과 문서의 총 개수를 의미한다.
    val start : Int, // 검색 결과 문서 중, 문서의 시작점을 의미한다.
    val display : Int, // 검색된 검색 결과의 개수이다.
    val items : List<NewsItems>, //개별 검색 결과이며 title, originallink, link, description, pubDate를 포함한다.
) {
    fun mapper() = items.map { it.mapper() }
}

data class NewsItems(
    val title : String, // 개별 검색 결과
    @SerializedName("originallink")
    val originalLink: String, // 검색 결과 문서의 제공 언론사 하이퍼텍스트 link를 나타낸다.
    val link: String, // 검색 결과 문서의 제공 네이버 하이퍼텍스트 link를 나타낸다.
    val description : String, //검색 결과 문서의 내용을 요약한 패시지 정보이다. 문서 전체의 내용은 link를 따라가면 읽을 수 있다. 패시지에서 검색어와 일치하는 부분은 태그로 감싸져 있다.
    val pubDate : String // 검색 결과 문서가 네이버에 제공된 시간이다.
) {
    fun mapper() = NewsContents(
        title = title,
        url = originalLink,
        description = description
    )
}

data class NewsContents(
    val title: String,
    val url: String,
    val description: String
)