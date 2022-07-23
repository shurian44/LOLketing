package com.ezen.lolketing.network

import android.net.Uri
import android.util.Log
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.Grade
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore
) {

    private fun getCurrentUser() = auth.currentUser

    fun getUserEmail() = getCurrentUser()?.email

    fun getStorageReference(path: String) = storage.reference.child(path)

    fun getFirestore(collection: String) = firestore.collection(collection)

    suspend fun joinUser(
        email: String,
        pw: String,
        successListener: (String?) -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val result = auth.createUserWithEmailAndPassword(email, pw).await()
            successListener(result.user?.uid)
        } catch (e: Exception) {
            failureListener()
            e.printStackTrace()
        }
    }

    suspend fun registerUser(
        email: String,
        uid: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val user = Users().apply {
                id = email
                this.uid = uid
                grade = Grade.BRONZE.gradeCode
            }

            firestore
                .collection(Constants.USERS)
                .document(email)
                .set(user)
                .await()

            successListener()

        } catch (e: Exception) {
            failureListener()
            e.printStackTrace()
        }
    }

    suspend fun deleteUser(
        successListener: () -> Unit,
        failureListener: (() -> Unit)? = null
    ) {
        try {
            getCurrentUser()
                ?.delete()
                ?.await()
                ?: failureListener?.invoke()

            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getUserInfo(
        successListener: (Users) -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val email = auth.currentUser?.email
            if (email == null) {
                failureListener()
                return
            }

           val result =  firestore
                .collection(Constants.USERS)
                .document(email)
                .get()
                .await()

            result.toObject(Users::class.java)
                ?.let(successListener)
                ?: failureListener()

        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun getUserNickName(): String? = try {
        val email = getCurrentUser()?.email ?: throw Exception("회원 정보를 찾지 못했습니다.")

        firestore
            .collection(Constants.USERS)
            .document(email)
            .get()
            .await()
            .toObject(Users::class.java)
            ?.nickname
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBasicSnapshot(
        collection: String,
        document: String,
        successListener: (DocumentSnapshot) -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val result = firestore
                .collection(collection)
                .document(document)
                .get()
                .await()

            successListener(result)
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun getBasicSnapshot(
        collection: String,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val result = firestore
                .collection(collection)
                .get()
                .await()

            successListener(result)
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun getBasicQuerySnapshot(
        collection: String,
        field: String,
        query: Any,
        orderByField: String = TIME_STAMP,
        orderByDirection: Query.Direction = Query.Direction.DESCENDING,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val result = firestore
                .collection(collection)
                .whereEqualTo(field, query)
                .orderBy(orderByField, orderByDirection)
                .get()
                .await()

            successListener(result)
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun basicAddData(
        collection: String,
        data: Any,
        successListener: ((DocumentReference) -> Unit)? = null,
        failureListener: (() -> Unit)? = null
    ) {
        try {
            val result = firestore
                .collection(collection)
                .add(data)
                .await()

            successListener?.invoke(result)
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener?.invoke()
        }
    }

    suspend fun basicAddData(
        collection: String,
        document: String,
        data: Any,
        successListener: (() -> Unit)? = null,
        failureListener: (() -> Unit)? = null
    ) {
        try {
            firestore
                .collection(collection)
                .document(document)
                .set(data)
                .await()

            successListener?.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener?.invoke()
        }
    }

    suspend fun basicUpdateData(
        collection: String,
        documentId: String,
        updateData: Map<String, Any?>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            firestore
                .collection(collection)
                .document(documentId)
                .update(updateData)
                .await()

            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun basicDeleteData(
        collection: String,
        documentId: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            firestore
                .collection(collection)
                .document(documentId)
                .delete()
                .await()

            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun getBasicSearchData(
        collection: String,
        field: String,
        startDate: String,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val result = firestore
                .collection(collection)
                .orderBy(field)
                .startAt(startDate)
                .endAt("$startDate\\uf8ff")
                .get()
                .await()

            successListener(result)
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun basicDelete(
        collection: String,
        documentId: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            firestore
                .collection(collection)
                .document(documentId)
                .delete()
                .await()

            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun getDoubleSnapshot(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        orderByField: String = TIME_STAMP,
        orderByDirection: Query.Direction = Query.Direction.DESCENDING,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val result = firestore
                .collection(firstCollection)
                .document(firstDocument)
                .collection(secondCollection)
                .orderBy(orderByField, orderByDirection)
                .get()
                .await()

            successListener(result)
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun doubleAddData(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        data: Any,
        successListener: (() -> Unit)? = null,
        failureListener: (() -> Unit)? = null
    ) {
        try {
            firestore
                .collection(firstCollection)
                .document(firstDocument)
                .collection(secondCollection)
                .document()
                .set(data)
                .await()

            successListener?.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener?.invoke()
        }
    }

    suspend fun doubleUpdateData(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        secondDocument: String,
        updateData: Map<String, Any?>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            firestore
                .collection(firstCollection)
                .document(firstDocument)
                .collection(secondCollection)
                .document(secondDocument)
                .update(updateData)
                .await()

            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun doubleDelete(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        secondDocument: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            firestore
                .collection(firstCollection)
                .document(firstDocument)
                .collection(secondCollection)
                .document(secondDocument)
                .delete()
                .await()

            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun storageDelete(
        path: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            storage
                .reference
                .child(path)
                .delete()
                .await()

            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun basicFileUpload(
        fileName: String,
        uri: Uri,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val storageRef = getStorageReference(fileName)

            storageRef
                .putFile(uri)
                .await()

            val result = storageRef
                .downloadUrl
                .await()

            successListener(result.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    companion object {
        const val TIME_STAMP = "timestamp"
    }

}