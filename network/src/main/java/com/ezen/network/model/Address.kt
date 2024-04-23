package com.ezen.network.model

import com.google.gson.annotations.SerializedName

/**
 * 주소 검색 api 결과
 * */
data class AddressResult(
    val results : AddressData
)

data class AddressData(
    val common : AddressCommon,
    @SerializedName("juso")
    val address : List<Address>?
) {
    fun toSearchResult() : SearchResult =
        SearchResult(
            list = address?.mapNotNull { it.roadAddr } ?: listOf(),
            isMoreData = (common.totalCount?.toInt() ?: 0) > (common.countPerPage ?: 10) * (common.currentPage?.toInt() ?: 0)
        )
}

data class AddressCommon(
    val totalCount : String?, // 총 검색 데이터수
    val currentPage : String?, // 페이지 번호
    val countPerPage : Int?, // 페이지당 출력할 결과 Row 수
    val errorCode : String?, // 에러 코드
    val errorMessage : String? // 에러 메시지
)

data class Address(
    val roadAddr : String?, // 전체 도로명주소
    val roadAddrPart1 : String?, // 도로명주소(참고항목 제외)
    val roadAddrPart2 : String?, // 도로명주소 참고항목
    val jibunAddr : String?, // 지번주소
    val engAddr : String?, // 도로명주소(영문)
    val zipNo : String?, // 우편번호
    val admCd : String?, // 행정구역코드
    val rnMgtSn : String?, // 도로명코드
    val bdMgtSn : String?, // 건물관리번호
    val detBdNmList : String?, // 상세건물명
    val bdNm : String?, // 건물명
    val bdKdcd : String?, // 공동주택여부(1 : 공동주택, 0 : 비공동주택)
    val siNm : String?, // 시도명
    val sggNm : String?, // 시군구명
    val emdNm : String?, // 읍면동명
    val liNm : String?, // 법정리명
    val rn : String?, // 도로명
    val udrtYn : String?, // 지하여부(0 : 지상, 1 : 지하)
    val buldMnnm : Long?, // 건물본번
    val buldSlno : Long?, // 건물부번
    val mtYn : String?, // 산여부(0 : 대지, 1 : 산)
    val lnbrMnnm : Long?, // 지번본번(번지)
    val lnbrSlno : Long?, // 지번부번(호)
    val emdNo : String?, // 읍면동일련번호
    val hstryYn : String?, // [2020년12월8일 추가된 항목] 변동이력여부(0: 현행 주소정보, 1: 요청변수의 keyword(검색어)가 변동된 주소정보에서 검색된 정보)
    val relJibun : String?, // [2020년12월8일 추가된 항목] 관련지번
    val hemdNm : String? // [2020년12월8일 추가된 항목] 관할주민센터 ※ 참고정보이며, 실제와 다를 수 있습니다.
)

data class SearchResult(
    val list : List<String>,
    val isMoreData : Boolean
)

data class AddressSearchInfo(
    var keyword: String = "",
    var isSearchMode: Boolean = true,
    var address: String = "",
    var addressDetail: String = ""
) {
    fun getFullAddress() = runCatching {
        if (address.isEmpty()) throw Exception("주소를 입력해주세요")
        addressDetail.takeUnless { it.isEmpty() }?.let { "$address, $it" } ?: address
    }
}