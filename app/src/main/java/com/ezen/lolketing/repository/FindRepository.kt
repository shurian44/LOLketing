package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindRepository @Inject constructor(
    private val client: FirebaseClient
) {

    fun findPassword(email: String) = flow {
        client
            .getBasicSearchData(
                collection = Constants.USERS,
                field = Constants.ID,
                startDate = email,
                valueType = Users::class.java
            )
            .getOrThrow()
            .ifEmpty { throw Exception("아이디를 확인해주세요.") }

        client
            .sendPasswordResetEmail(email)
            .getOrThrow()

        emit("이메일로 비밀번호 재설정 메일을 전송하였습니다.")
    }

}