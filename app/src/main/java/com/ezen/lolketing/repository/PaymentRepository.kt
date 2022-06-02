package com.ezen.lolketing.repository

import com.ezen.lolketing.model.PurchaseDTO
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val client: FirebaseClient
) {

    suspend fun updateSeat(
        firstDocumentId: String,
        secondDocumentId: String,
        data: String,
        onSuccessListener: () -> Unit,
        onFailureListener: () -> Unit
    ) {
        firestore
            .collection(Constants.GAME)
            .document(firstDocumentId)
            .collection(Constants.SEAT)
            .document(secondDocumentId)
            .update("reserveId", data)
            .addOnSuccessListener{
                onSuccessListener()
            }
            .addOnFailureListener {
                it.printStackTrace()
                onFailureListener()
            }
            .await()
    }

    suspend fun generateQrCode(
        path: String,
        data: ByteArray,
        onSuccessListener: (String) -> Unit,
        onFailureListener: () -> Unit
    ) {
        val storageRef = client
            .getStorageReference(path)

        storageRef.putBytes(data)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnCompleteListener { task ->
                    task.result.toString()
                    onSuccessListener(task.result.toString())
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                onFailureListener()
            }
            .await()
    }

    suspend fun setPurchase(
        data: PurchaseDTO,
        successListener : () -> Unit,
        failureListener : () -> Unit
    ) {
        client
            .basicAddData(
                collection = Constants.PURCHASE,
                data = data,
                successListener = {
                    successListener()
                },
                failureListener = failureListener
            )
    }

}