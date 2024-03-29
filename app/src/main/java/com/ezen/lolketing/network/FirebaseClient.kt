package com.ezen.lolketing.network

import android.net.Uri
import com.ezen.lolketing.model.Game
import com.ezen.lolketing.model.LoginInfo
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.Grade
import com.ezen.lolketing.util.LoginException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val fireStore: FirebaseFirestore
) {

    private fun getCurrentUser() = auth.currentUser

    fun getUserEmail() = getCurrentUser()?.email

    fun getStorageReference(path: String) = storage.reference.child(path)

    fun getFireStore(collection: String) = fireStore.collection(collection)

    suspend fun joinUser(
        email: String,
        pw: String,
    ) = runCatching {
        auth.createUserWithEmailAndPassword(email, pw)
            .await()
            .user?.uid
            ?: throw LoginException.FailureJoin
    }

    suspend fun registerUser(
        email: String,
        uid: String
    ) = runCatching {
        val user = Users().also {
            it.id = email
            it.uid = uid
            it.grade = Grade.BRONZE.gradeCode
        }

        fireStore
            .collection(Constants.USERS)
            .document(email)
            .set(user)
            .await()
    }

    suspend fun emailLogin(info: LoginInfo) = runCatching {
        auth
            .signInWithEmailAndPassword(info.id, info.password)
            .await()
    }

    suspend fun sendPasswordResetEmail(email: String) = runCatching {
        auth
            .sendPasswordResetEmail(email)
            .await()
    }

    suspend fun deleteUser() = runCatching {
        getCurrentUser()
            ?.delete()
            ?.await()
            ?: LoginException.DeleteUserError
    }

    suspend fun getUserInfo() = runCatching {
        val email = getUserEmail() ?: throw LoginException.EmptyUser
        fireStore
            .collection(Constants.USERS)
            .document(email)
            .get()
            .await()
            .toObject(Users::class.java)
            ?: throw LoginException.EmptyUser
    }

    suspend fun getUserNickName() = runCatching {
        val email = getCurrentUser()?.email ?: throw LoginException.EmptyUser

        fireStore
            .collection(Constants.USERS)
            .document(email)
            .get()
            .await()
            .toObject(Users::class.java)
            ?.nickname
            ?: throw LoginException.EmptyUser
    }

    fun signOut() = auth.signOut()

    suspend fun fetchTicketList(date: String) = runCatching {
        fireStore
            .collection(Constants.GAME)
            .orderBy(Constants.DATE)
            .whereGreaterThan(Constants.DATE, date)
            .get()
            .await()
            .mapNotNull { it.toObject(Game::class.java).mapper() }
    }

    suspend fun <T> getBasicSnapshot(
        collection: String,
        document: String,
        valueType: Class<T>
    ) = runCatching {
        fireStore
            .collection(collection)
            .document(document)
            .get()
            .await()
            .toObject(valueType)
    }

    suspend fun <T> getBasicSnapshot(
        collection: String,
        valueType: Class<T>
    ) = runCatching {
        fireStore
            .collection(collection)
            .get()
            .await()
            .mapNotNull { Pair(it.toObject(valueType), it.id) }
    }

    suspend fun <T> getBasicQuerySnapshot(
        collection: String,
        field: String,
        query: Any,
        orderByField: String = TIME_STAMP,
        orderByDirection: Query.Direction = Query.Direction.DESCENDING,
        valueType: Class<T>
    ) = runCatching {
        fireStore
            .collection(collection)
            .whereEqualTo(field, query)
            .orderBy(orderByField, orderByDirection)
            .get()
            .await()
            .mapNotNull { Pair(it.toObject(valueType), it.id) }
    }

    suspend fun basicAddData(
        collection: String,
        data: Any
    ) = runCatching {
        fireStore
            .collection(collection)
            .add(data)
            .await()
    }

    suspend fun basicAddData(
        collection: String,
        document: String,
        data: Any
    ) = runCatching {
        fireStore
            .collection(collection)
            .document(document)
            .set(data)
            .await()
    }

    suspend fun basicUpdateData(
        collection: String,
        documentId: String,
        updateData: Map<String, Any?>
    ) = runCatching {
        fireStore
            .collection(collection)
            .document(documentId)
            .update(updateData)
            .await()
    }

    suspend fun basicDeleteData(
        collection: String,
        documentId: String
    ) = runCatching {
        fireStore
            .collection(collection)
            .document(documentId)
            .delete()
            .await()
    }

    suspend fun <T> getBasicSearchData(
        collection: String,
        field: String,
        startDate: String,
        valueType: Class<T>
    ) = runCatching {
        fireStore
            .collection(collection)
            .orderBy(field)
            .startAt(startDate)
            .endAt("$startDate\\uf8ff")
            .get()
            .await()
            .mapNotNull { Pair(it.toObject(valueType), it.id) }
    }

    suspend fun basicDelete(
        collection: String,
        documentId: String
    ) = runCatching {
        fireStore
            .collection(collection)
            .document(documentId)
            .delete()
            .await()
    }

    suspend fun <T> getDoubleSnapshot(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        orderByField: String = TIME_STAMP,
        orderByDirection: Query.Direction = Query.Direction.DESCENDING,
        valueType: Class<T>
    ) = runCatching {
        fireStore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .orderBy(orderByField, orderByDirection)
            .get()
            .await()
            .mapNotNull { Pair(it.toObject(valueType), it.id) }
    }

    suspend fun doubleAddData(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        data: Any
    ) = runCatching {
        fireStore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .document()
            .set(data)
            .await()
    }

    suspend fun doubleUpdateData(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        secondDocument: String,
        updateData: Map<String, Any?>
    ) = runCatching {
        fireStore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .document(secondDocument)
            .update(updateData)
            .await()
    }

    suspend fun doubleDelete(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        secondDocument: String
    ) = runCatching {
        fireStore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .document(secondDocument)
            .delete()
            .await()
    }

    suspend fun storageDelete(path: String) = runCatching {
        storage
            .reference
            .child(path)
            .delete()
            .await()
    }

    suspend fun basicFileUpload(
        fileName: String,
        uri: Uri
    ) = runCatching {
        val storageRef = getStorageReference(fileName)

        storageRef
            .putFile(uri)
            .await()

        storageRef
            .downloadUrl
            .await()
            .toString()
    }

    suspend fun basicFileUpload(
        fileName: String,
        data: ByteArray
    ) = runCatching{
        val storageRef = getStorageReference(fileName)

        storageRef
            .putBytes(data)
            .await()

        storageRef
            .downloadUrl
            .await()
            .toString()
    }

    companion object {
        const val TIME_STAMP = "timestamp"
    }

}