package com.ezen.lolketing.network

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

    suspend fun getBasicQuerySnapshot(
        collection : String,
        field: String,
        query : Any,
        orderByField: String = TIME_STAMP,
        orderByDirection : Query.Direction = Query.Direction.DESCENDING
    ) : QuerySnapshot? = try {
        firestore.collection(collection).whereEqualTo(field, query).orderBy(orderByField, orderByDirection).get().await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBasicQuerySnapshot(
        collection : String,
        queryList : List<Pair<String, Any>>,
        orderByField: String = TIME_STAMP,
        orderByDirection : Query.Direction = Query.Direction.DESCENDING
    ) : QuerySnapshot? = try {
        val reference = firestore.collection(collection)
        queryList.forEach {
            reference.whereEqualTo(it.first, it.second)
        }
        reference.orderBy(orderByField, orderByDirection).get().await()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }


    companion object {
        const val TIME_STAMP = "timestamp"
    }

}