package com.ezen.lolketing.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val auth : FirebaseAuth,
    private val storage : FirebaseStorage,
    private val firestore: FirebaseFirestore
) {

    suspend fun getCurrentUser() =
        auth.currentUser

    suspend fun getUserInfo(
        collectionPath : String,
        documentPath: String,
    ) : DocumentSnapshot? = try{
        firestore.collection(collectionPath).document(documentPath).get().await()
//        documentSnapshot.toObject(Users::class.java)
    } catch (e : Exception) {
        e.printStackTrace()
        null
    }

}
//
//class FireBaseSource @Inject constructor(private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore) {
//
//    fun signUpUser(email:String,password:String,fullName:String) = firebaseAuth.createUserWithEmailAndPassword(email,password)
//
//    fun signInUser(email: String,password: String) = firebaseAuth.signInWithEmailAndPassword(email,password)
//
//    fun saveUser(email: String,name:String)=firestore.collection("users").document(email).set(User(email = email,fullName = name))
//}