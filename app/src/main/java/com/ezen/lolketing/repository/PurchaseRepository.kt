package com.ezen.lolketing.repository

import com.ezen.lolketing.model.PurchaseDTO
import com.ezen.lolketing.model.TicketInfo
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class PurchaseRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getTicketInfo(
        documentId: String,
        successListener: (TicketInfo) -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .getBasicSnapshot(
                collection = Constants.PURCHASE,
                document = documentId,
                successListener = {
                    it.toObject(PurchaseDTO::class.java)?.let { purchase ->
                        successListener(purchase.ticketMapper())
                    } ?: failureListener()
                },
                failureListener = failureListener
            )
    }

    suspend fun updateUserCache(
        refund: Long,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        val email = client.getUserEmail()

        if (email == null) {
            failureListener()
            return
        }

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = email,
                updateData = mapOf(CACHE to FieldValue.increment(refund)),
                successListener = successListener,
                failureListener = failureListener
            )
    }

    suspend fun updateSeatInfo(
        time: String,
        documentIdList: List<String>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        documentIdList.forEach {
            client
                .doubleUpdateData(
                    firstCollection = Constants.GAME,
                    firstDocument = time,
                    secondCollection = Constants.SEAT,
                    secondDocument = it,
                    updateData = mapOf(DOCUMENT_ID to "", RESERVE_ID to ""),
                    successListener = {},
                    failureListener = failureListener
                )
        }
        successListener()
    }

    suspend fun deletePurchase(
        documentId: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .basicDelete(
                collection = Constants.PURCHASE,
                documentId = documentId,
                successListener = successListener,
                failureListener = failureListener
            )

    }

    suspend fun deleteQrCode(
        path: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .storageDelete(
                path = path,
                successListener = successListener,
                failureListener = failureListener
            )
    }

    companion object {
        const val CACHE = "cache"
        const val DOCUMENT_ID = "documentId"
        const val RESERVE_ID = "reserveId"
    }

}