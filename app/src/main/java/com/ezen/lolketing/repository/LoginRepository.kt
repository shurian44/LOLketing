package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.view.login.join.JoinDetailViewModel
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val client: FirebaseClient
) {

    fun getCurrentUser() = client.getCurrentUser()

    suspend fun joinUser(
        email: String,
        pw: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.joinUser(
            email= email,
            pw= pw,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun registerUser(
        email: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.registerUser(
            email = email,
            successListener= successListener,
            failureListener = failureListener
        )
    }

    suspend fun deleteUser(
        successListener: () -> Unit,
    ) {
        client.deleteUser(
            successListener= successListener
        )
    }

    suspend fun getUserInfo(
        email: String,
        successListener: (JoinDetailViewModel.Event.UserInfo) -> Unit,
        failureListener: (String) -> Unit
    ) {
        client.getBasicSnapshot(
            collection = Constants.USERS,
            document = email,
            successListener= {
                val user = it.toObject(Users::class.java)
                if (user == null) {
                    failureListener(email)
                } else {
                    successListener(user.toUserInfo())
                }
            },
            failureListener= {
                failureListener(email)
            }
        )
    }

    suspend fun updateModifyUserInfo(
        user : Users,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        val uid = user.uid
        if (uid == null) {
            failureListener()
            return
        }

        client.basicUpdateData(
            collection = Constants.USERS,
            documentId = uid,
            updateData = mapOf("nickname" to user.nickname, "phone" to user.phone, "address" to user.address),
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun updateNewUserInfo(
       user: Users,
       successListener: () -> Unit,
       failureListener: () -> Unit
    ) {
        val uid = user.uid
        if (uid == null) {
            failureListener()
            return
        }
    }

    private fun Users.toUserInfo() =
        JoinDetailViewModel.Event.UserInfo(
            nickname = nickname,
            phone = phone,
            address = address
        )

//            firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnCompleteListener {
//            user = it.result?.toObject(Users::class.java)!!
//            editNickname.setText(user.nickname)
//            editPhone.setText(user.phone)
//            editAddress.setText(user.address)
//            btnRegister.text = getString(R.string.modify)
//        }

}