package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Board
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val client: FirebaseClient
){

    suspend fun getUserNickname() : String? =
        client.getUserNickName()

    suspend fun getBoardList(
        field : String = Constants.TEAM,
        query : String,
        successListener : (QuerySnapshot) -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.getBasicQuerySnapshot(
            collection = Constants.BOARD,
            field = field,
            query = query,
            successListener = {
                successListener(it)
            },
            failureListener = {
                failureListener()
            }
        )
    } catch (e : Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBoardList(
        queryList: List<Pair<String, Any>>,
        successListener : (QuerySnapshot) -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.getBasicQuerySnapshot(
            collection = Constants.BOARD,
            queryList = queryList,
            successListener = {
                successListener(it)
            },
            failureListener = {
                failureListener()
            }
        )
    } catch (e : Exception) {
        e.printStackTrace()
        null
    }

    suspend fun uploadBoard(
        data : Board,
        successListener : () -> Unit,
        errorListener : () -> Unit
    ) = try {
        client.basicAddData(Constants.BOARD, data)?.addSnapshotListener { value, error ->
            error?.let {
                throw Exception(it)
            }
            value?.let {
                successListener()
                // DocumentSnapshot{key=Board/Lq9cjEfT5aTbHWFuwiS1, metadata=SnapshotMetadata{hasPendingWrites=false, isFromCache=true}, doc=Document{key=Board/Lq9cjEfT5aTbHWFuwiS1, version=SnapshotVersion(seconds=1652515866, nanos=339083000), readTime=SnapshotVersion(seconds=1652515866, nanos=339083000), type=FOUND_DOCUMENT, documentState=HAS_COMMITTED_MUTATIONS, value=ObjectValue{internalValue={commentCounts:0,content:ㅎㅎ,documentId:null,email:test@test.com,image:null,like:{},likeCounts:0,subject:[게시판],team:T1,timestamp:1652515865469,title:ㄱㄱ,userId:알렌,views:0}}}}
            }
        } ?: throw Exception("업로드 과정 중 오류가 발생하였습니다.")
    } catch (e: Exception) {
        e.printStackTrace()
        errorListener()
    }

    suspend fun updateBoard(
        documentId: String,
        updateData: Map<String, Any>,
        successListener: () -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.basicUpdateData(
            collection = Constants.BOARD,
            documentId = documentId,
            updateData = updateData,
            successListener = successListener,
            failureListener = failureListener
        )
    } catch (e : Exception) {
        e.printStackTrace()
    }

    suspend fun deleteBoard(
        documentId: String,
        successListener: () -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.basicDeleteData(
            collection = Constants.BOARD,
            documentId = documentId,
            successListener = {
                successListener()
            },
            failureListener = {
                failureListener()
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}