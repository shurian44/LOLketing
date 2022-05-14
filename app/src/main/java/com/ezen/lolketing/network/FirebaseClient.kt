package com.ezen.lolketing.network

import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val auth : FirebaseAuth,
    private val storage : FirebaseStorage,
    private val firestore: FirebaseFirestore
) {

    fun getCurrentUser() = auth.currentUser

    suspend fun getUserInfo(
        collectionPath : String,
        documentPath: String,
    ) : DocumentSnapshot? = try{
        firestore.collection(collectionPath).document(documentPath).get().await()
    } catch (e : Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getUserNickName() : String? = try {
        val email = getCurrentUser()?.email ?: throw Exception("회원 정보를 찾지 못했습니다.")
        firestore.collection(Constants.USERS).document(email).get().await().toObject(Users::class.java)?.nickname
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBasicQuerySnapshot(
        collection : String,
        field: String,
        query : Any,
        orderByField: String = TIME_STAMP,
        orderByDirection : Query.Direction = Query.Direction.DESCENDING,
        successListener : (QuerySnapshot) -> Unit,
        failureListener : () -> Unit
    ) : QuerySnapshot? = try {
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
        collection : String,
        queryList : List<Pair<String, Any>>,
        orderByField: String = TIME_STAMP,
        orderByDirection : Query.Direction = Query.Direction.DESCENDING,
        successListener : (QuerySnapshot) -> Unit,
        failureListener : () -> Unit
    ) : QuerySnapshot? = try {
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
        data : Any
    ) = try {
        firestore.collection(collection).add(data).await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun basicUpdateData(
        collection: String,
        documentId : String,
        updateData : Map<String, Any>,
        successListener : () -> Unit,
        failureListener : () -> Unit
    ) = try {
        firestore
            .collection(collection).document(documentId).update(updateData)
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
        successListener : () -> Unit,
        failureListener : () -> Unit
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
    } catch (e: Exception){
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

    companion object {
        const val TIME_STAMP = "timestamp"
    }

}