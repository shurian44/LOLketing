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

        const val TypeId = "id"
        const val TypePassword = "password"
        const val TypePasswordCheck = "passwordCheck"
        const val TypeNickname = "nickname"
        const val TypeMobile = "mobile"
        const val TypeAddress = "address"
    }

    fun checkValidationException() {
        val message = checkValidation()
        if (message.isNotEmpty()) {
            throw Exception(message)
        }
    }

    fun checkValidation() = when {
        isEmailValid(id).not() -> "아이디는 이메일 형식으로 입력해 주세요."
        id.length > 100 -> "아이디는 100자 이하로 설정해주세요."
        isEmailType() && isPasswordValid(password).not() ->
            "비밀번호는 영문, 숫자, 특수문자가 모두 포함되게 입력해 주세요."
        isEmailType() && password.length !in 6..30 ->
            "비밀번호는 6자 이상 30자 이하로 입력해 주세요."
        isEmailType() && isPasswordCheckValid(password, passwordCheck).not() ->
            "비밀번호와 비밀번호 확인이 일치하지 않습니다."
        nickname.isEmpty() || isNicknameValid(nickname).not() ->
            "닉네임은 1~11자 이상으로 입력해 주세요."
        mobile.isNotEmpty() && isMobileValid(mobile).not() ->
            "전화번호를 확인해 주세요."
        else -> ""
    }

    fun isEmailType() = type == UserInfoType.Email.type

    fun isEmailValid(value: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return emailRegex.matches(value)
    }

    fun isPasswordValid(value: String): Boolean {
        val regex = Regex("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()]).{3,30}$")
        return regex.matches(value)
    }

    fun isPasswordCheckValid(password: String, passwordCheck: String) =
        password == passwordCheck

    fun isNicknameValid(nickname: String) =
        nickname.length in 1..11

    fun isMobileValid(mobile: String) =
        mobile.length in 10..11

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
