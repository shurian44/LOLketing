package com.ezen.network.client

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val storage: FirebaseStorage,
    private val fireStore: FirebaseFirestore
) {
    private fun getStorageReference(path: String) = storage.reference.child(path)

    fun getFireStore(collection: String) = fireStore.collection(collection)

    suspend fun basicAddData(
        collection: String,
        data: Any
    ) = runCatching {
        fireStore
            .collection(collection)
            .add(data)
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
}