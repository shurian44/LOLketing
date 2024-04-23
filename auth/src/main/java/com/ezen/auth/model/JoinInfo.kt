package com.ezen.auth.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JoinInfo(
    val type: String,
    val id: String,
    val password: String,
    val passwordCheck: String,
    val nickname: String,
    val gender: String,
    val birthday: String,
    val birthYear: String,
    val mobile: String,
    val address: String,
) : Parcelable {
    companion object {
        fun create() = JoinInfo(
            type = "",
            id = "",
            password = "",
            passwordCheck = "",
            nickname = "",
            gender = "",
            birthday = "",
            birthYear = "",
            mobile = "",
            address = "",
        )
    }

    fun checkValidation() = when {
        isEmailValid().not() -> throw Exception("아이디는 이메일 형식으로 입력해 주세요.")
        id.length > 100 -> throw Exception("아이디는 100자 이하로 설정해주세요.")
        isEmailType() && isPasswordValid().not() ->
            throw Exception("비밀번호는 영문, 숫자, 특수문자가 모두 포함되게 입력해 주세요.")
        isEmailType() && password.length !in 6..30 ->
            throw Exception("비밀번호는 6자 이상 30자 이하로 입력해 주세요.")
        isEmailType() && password != passwordCheck ->
            throw Exception("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
        nickname.isEmpty() || nickname.length >= 11 ->
            throw Exception("닉네임은 1~11자 이상으로 입력해 주세요.")
        mobile.isNotEmpty() && mobile.length !in 10..11 -> throw Exception("전화번호를 확인해 주세요.")
        else -> true
    }

    private fun isEmailType() = type == UserInfoType.Email.type

    fun isEmailValid(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return emailRegex.matches(id)
    }

    fun isPasswordValid(): Boolean {
        val regex = Regex("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()]).{3,30}$")
        return regex.matches(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(type)
        parcel.writeString(id)
        parcel.writeString(password)
        parcel.writeString(nickname)
        parcel.writeString(gender)
        parcel.writeString(birthday)
        parcel.writeString(birthYear)
        parcel.writeString(mobile)
        parcel.writeString(address)
    }
}

enum class UserInfoType(val type: String) {
    Email("email"),
    Naver("naver"),
    Kakao("kakao")
}
