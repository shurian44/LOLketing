package com.ezen.lolketing.repository

import com.ezen.lolketing.model.CacheModifyUser
import com.ezen.lolketing.model.PurchaseDTO
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getUserCache(
        successListener: (Long) -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .getUserInfo(
                successListener = {
                    it.cache?.let(successListener)?:failureListener()
                },
                failureListener = {
                    failureListener()
                }
            )
    }

    suspend fun getUserInfo(
        successListener: (CacheModifyUser) -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .getUserInfo(
                successListener = {
                    successListener(it.mapper())
                },
                failureListener = {
                    failureListener()
                }
            )
    }

    suspend fun updateSeat(
        firstDocumentId: String,
        secondDocumentId: String,
        data: String,
        onSuccessListener: () -> Unit,
        onFailureListener: () -> Unit
    ) {
        try {
            client
                .getFirestore(Constants.GAME)
                .document(firstDocumentId)
                .collection(Constants.SEAT)
                .document(secondDocumentId)
                .update("reserveId", data)
                .await()

            onSuccessListener()
        } catch (e: Exception) {
            e.printStackTrace()
            onFailureListener()
        }
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
        successListener : (String) -> Unit,
        failureListener : () -> Unit
    ) {
        client
            .basicAddData(
                collection = Constants.PURCHASE,
                data = data,
                successListener = {
                    successListener(it.id)
                },
                failureListener = failureListener
            )
    }

    suspend fun myCacheDeduction(
        deductionCache: Long,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        val email = client.getUserEmail()

        if (email == null){
            failureListener()
            return
        }

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = email,
                updateData = mapOf(
                    CACHE to FieldValue.increment(-deductionCache),
                    ROULETTE_COUNT to FieldValue.increment(if (deductionCache > 11_000) 2 else 1)
                ),
                successListener = successListener,
                failureListener = failureListener
            )
    }

    suspend fun updateChargingCache(
        addCache: Long,
        point: Long,
        grade: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        val email = client.getUserEmail()

        if (email == null){
            failureListener()
            return
        }

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = email,
                updateData = mapOf(
                    CACHE to FieldValue.increment(addCache),
                    POINT to point,
                    GRADE to grade
                ),
                successListener = successListener,
                failureListener = failureListener
            )
    }

    companion object {
        const val CACHE = "cache"
        const val POINT = "point"
        const val GRADE = "grade"
        const val ROULETTE_COUNT = "rouletteCount"
    }

}