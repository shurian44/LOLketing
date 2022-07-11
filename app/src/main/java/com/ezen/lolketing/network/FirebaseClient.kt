package com.ezen.lolketing.network

import android.net.Uri
import android.util.Log
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.Constants
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

    fun getCurrentUser() = auth.currentUser

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
            auth
                .createUserWithEmailAndPassword(email, pw)
                .addOnSuccessListener {
                    successListener(it.user?.uid)
                }
                .addOnFailureListener {
                    failureListener()
                }
                .await()
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
                grade = Constants.BRONZE
            }

            firestore
                .collection(Constants.USERS)
                .document(email)
                .set(user)
                .addOnSuccessListener {
                    successListener()
                }
                .addOnFailureListener {
                    failureListener()
                }
                .await()
        } catch (e: Exception) {
            failureListener()
            e.printStackTrace()
        }
    }

    suspend fun registerUser(
        user: Users,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val id = user.id
            if (id == null) {
                failureListener()
                return
            }

            firestore
                .collection(Constants.USERS)
                .document(id)
                .set(user)
                .addOnSuccessListener {
                    successListener()
                }
                .addOnFailureListener {
                    failureListener()
                }
                .await()
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
                ?.addOnSuccessListener { successListener() }
                ?.addOnFailureListener { failureListener?.invoke() }
                ?.await()
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

            firestore
                .collection(Constants.USERS)
                .document(email)
                .get()
                .addOnSuccessListener {
                    it.toObject(Users::class.java)
                        ?.let(successListener)
                        ?: failureListener()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    failureListener()
                }
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun getUserNickName(): String? = try {
        val email = getCurrentUser()?.email ?: throw Exception("회원 정보를 찾지 못했습니다.")
        firestore.collection(Constants.USERS).document(email).get().await()
            .toObject(Users::class.java)?.nickname
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBasicSnapshot(
        collection: String,
        document: String,
        successListener: (DocumentSnapshot) -> Unit,
        failureListener: () -> Unit
    ) = try {
        firestore
            .collection(collection)
            .document(document)
            .get()
            .addOnSuccessListener {
                successListener(it)
            }
            .addOnFailureListener {
                failureListener()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBasicSnapshot(
        collection: String,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ) = try {
        firestore
            .collection(collection)
            .get()
            .addOnSuccessListener {
                successListener(it)
            }
            .addOnFailureListener {
                failureListener()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBasicQuerySnapshot(
        collection: String,
        field: String,
        query: Any,
        orderByField: String = TIME_STAMP,
        orderByDirection: Query.Direction = Query.Direction.DESCENDING,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ): QuerySnapshot? = try {
        firestore
            .collection(collection)
            .whereEqualTo(field, query)
            .orderBy(orderByField, orderByDirection)
            .get()
            .addOnSuccessListener {
                successListener(it)
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBasicQuerySnapshot(
        collection: String,
        queryList: List<Pair<String, Any>>,
        orderByField: String = TIME_STAMP,
        orderByDirection: Query.Direction = Query.Direction.DESCENDING,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ): QuerySnapshot? = try {
        val reference = firestore.collection(collection)
        queryList.forEach {
            reference.whereEqualTo(it.first, it.second)
        }
        reference
            .orderBy(orderByField, orderByDirection)
            .get()
            .addOnSuccessListener {
                successListener(it)
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun basicAddData(
        collection: String,
        data: Any,
        successListener: ((DocumentReference) -> Unit)? = null,
        failureListener: (() -> Unit)? = null
    ) = try {
        firestore
            .collection(collection)
            .add(data)
            .addOnSuccessListener {
                successListener?.invoke(it)
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener?.invoke()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun basicAddData(
        collection: String,
        document: String,
        data: Any,
        successListener: (() -> Unit)? = null,
        failureListener: (() -> Unit)? = null
    ) = try {
        firestore
            .collection(collection)
            .document(document)
            .set(data)
            .addOnSuccessListener {
                successListener?.invoke()
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener?.invoke()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun basicUpdateData(
        collection: String,
        documentId: String,
        updateData: Map<String, Any?>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        firestore
            .collection(collection)
            .document(documentId)
            .update(updateData)
            .addOnSuccessListener {
                successListener()
            }
            .addOnFailureListener {
                failureListener()
                it.printStackTrace()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun basicDeleteData(
        collection: String,
        documentId: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        firestore
            .collection(collection)
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                successListener()
            }
            .addOnFailureListener {
                failureListener()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBasicSearchData(
        collection: String,
        field: String,
        startDate: String,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ) {
        firestore
            .collection(collection)
            .orderBy(field)
            .startAt(startDate)
            .endAt("$startDate\\uf8ff")
            .get()
            .addOnSuccessListener {
                successListener(it)
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
    }

    suspend fun getBasicSearchData(
        collection: String,
        queryField: String,
        queryData: Any,
        orderByField: String,
        startDate: String,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ) {
        firestore
            .collection(collection)
            .whereEqualTo(queryField, queryData)
            .orderBy(orderByField)
            .startAt(startDate)
            .endAt("$startDate\\uf8ff")
            .get()
            .addOnSuccessListener {
                successListener(it)
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
    }

    suspend fun basicDelete(
        collection: String,
        documentId: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        firestore
            .collection(collection)
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                successListener()
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
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
        firestore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .orderBy(orderByField, orderByDirection)
            .get()
            .addOnSuccessListener(successListener)
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
    }

    suspend fun doubleAddData(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        secondDocument: String,
        data: Any,
        successListener: (() -> Unit)? = null,
        failureListener: (() -> Unit)? = null
    ) = try {
        firestore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .document(secondDocument)
            .set(data)
            .addOnSuccessListener {
                successListener?.invoke()
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener?.invoke()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun doubleAddData(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        data: Any,
        successListener: (() -> Unit)? = null,
        failureListener: (() -> Unit)? = null
    ) = try {
        firestore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .document()
            .set(data)
            .addOnSuccessListener {
                successListener?.invoke()
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener?.invoke()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun doubleUpdateData(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        secondDocument: String,
        updateData: Map<String, Any?>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        firestore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .document(secondDocument)
            .update(updateData)
            .addOnSuccessListener {
                successListener()
            }
            .addOnFailureListener {
                failureListener()
                it.printStackTrace()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun doubleDelete(
        firstCollection: String,
        firstDocument: String,
        secondCollection: String,
        secondDocument: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ): Any = try {
        firestore
            .collection(firstCollection)
            .document(firstDocument)
            .collection(secondCollection)
            .document(secondDocument)
            .delete()
            .addOnSuccessListener {
                successListener()
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun storageDelete(
        path: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        storage
            .reference
            .child(path)
            .delete()
            .addOnSuccessListener {
                successListener()
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun basicFileUpload(
        fileName: String,
        uri: Uri,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ): Any = try {
        val storageRef = getStorageReference(fileName)

        storageRef.putFile(uri)
            .addOnProgressListener {
                val progress = (100 * it.bytesTransferred / it.totalByteCount)
            }
            .addOnSuccessListener {
                storageRef
                    .downloadUrl
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            successListener(task.result.toString())
                        } else {
                            Log.e("basicFileUpload", "downloadUrl error")
                            failureListener()
                        }
                    }
            }
            .addOnFailureListener {
                it.printStackTrace()
                failureListener()
            }
            .await()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    companion object {
        const val TIME_STAMP = "timestamp"
    }

}