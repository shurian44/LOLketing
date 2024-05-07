package com.ezen.network.model

import java.text.DecimalFormat

data class StringIdParam(
    val id: String
)

data class IntIdParam(
    val id: Int
)

data class MyInfo(
    val nickname: String,
    val id: String,
    val mobile: String,
    val address: String,
    val lolketingId: Int,
    val grade: String,
    val point: Int,
    val cash: Int,
    val list: List<Coupon>,
    val totalCoupons: Int,
    val availableCoupons: Int
) {
    fun formatMobileNumber(): String {
        val regex = Regex("(\\d{3})(\\d{3,4})(\\d{4})")
        return mobile.replace(regex, "$1-$2-$3")
    }

    fun toModifyInfo() = ModifyInfo(
        id = id,
        nickname = nickname,
        mobile = mobile,
        address = address
    )

    fun getFormatCash() = DecimalFormat("###,###").format(cash).plus("원")

    fun getAvailableList() = list.filter { it.isUsed.not() }

    fun getUsedList() = list.filter { it.isUsed }

    fun getCouponInfo() = "${getAvailableList().size} / $totalCoupons"

    fun getGradeImage() = Grade.getImage(grade)

    fun getGradeName() = Grade.getKoreanName(grade)

    fun getMaxPoint() = Grade.getMaxPoint(grade)

    fun getGradeProgress() = "$point / ${getMaxPoint()}"

    companion object {
        fun init() = MyInfo(
            nickname = "",
            id = "",
            mobile = "",
            address = "",
            lolketingId = 0,
            grade = "",
            point = 0,
            cash = 0,
            list = listOf(),
            totalCoupons = 0,
            availableCoupons = 0
        )
    }
}

data class ModifyInfo(
    val id: String,
    val nickname: String,
    val mobile: String,
    val address: String
) {
    fun checkValidation() = when {
        id.isEmpty() -> throw Exception("유저 정보가 없습니다.")
        nickname.isEmpty() || nickname.length >= 11 ->
            throw Exception("닉네임은 1~11자 이상으로 입력해 주세요.")
        mobile.isNotEmpty() && mobile.length !in 10..11 ->
            throw Exception("전화번호를 확인해 주세요.")
        else -> true
    }

    companion object {
        fun init() = ModifyInfo(
            id =  "",
            nickname = "",
            mobile = "",
            address = ""
        )
    }
}