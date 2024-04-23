package com.ezen.network.service

import com.ezen.network.BuildConfig
import com.ezen.network.model.AddressResult
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressService {

    /**
     * 주소 검색
     * https://www.juso.go.kr/addrlink/devAddrLinkRequestGuide.do?menu=roadApi
     * @param confmKey : (필수) 신청시 발급받은 승인키
     * @param currentPage : (필수) 현재 페이지 번호, 기본 값 1
     * @param countPerPage : (필수) 페이지당 출력할 결과 Row 수, 기본 값 10
     * @param keyword : (필수) 주소 검색어
     * @param resultType : 검색결과형식 설정(xml, json), 기본 값 xml
     * @param hstryYn : [2020년12월8일 추가된 항목] 변동된 주소정보 포함 여부, 기본 값 N
     * @param firstSort : [2020년12월8일 추가된 항목] 정확도순 정렬(none)
     *                    우선정렬(road: 도로명 포함, location: 지번 포함), 기본 값 none
     *                    ※ keyword(검색어)가 우선정렬 항목에 포함된 결과 우선 표출
     * @param addInfoYn : [2020년12월8일 추가된 항목]
     *                    출력결과에 추가된 항목(hstryYn, relJibun, hemdNm) 제공여부, 기본 값 N
     *                    ※ 해당 옵션으로 추가제공되는 항목의 경우, 추후 특정항목이 제거되거나 추가될 수 있으니 적용 시 고려해주시기 바랍니다.
     * */
    @GET("addrlink/addrLinkApi.do")
    suspend fun fetchAddress(
        @Query("confmKey") confmKey : String? = BuildConfig.ADDRESS_API_KEY,
        @Query("currentPage") currentPage : Int? = 1,
        @Query("countPerPage") countPerPage : Int? = 10,
        @Query("keyword") keyword : String,
        @Query("resultType") resultType : String? = "json",
        @Query("firstSort") firstSort : String? = "road"
    ): AddressResult
}