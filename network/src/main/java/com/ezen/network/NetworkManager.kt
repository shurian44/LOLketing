package com.ezen.network

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.initialize

class NetworkManager {
    fun initFirebase(context: Context) {
        val options = FirebaseOptions.Builder()
            .setProjectId("lolketing")
            .setApplicationId("1:1007308432394:android:e88e729ff2a83d140bebf3")
            .setApiKey("AIzaSyDGcjMvjELKqkR-mi_w_yiA7kq0t4cp1ks")
            .build()

        Firebase.initialize(context, options, "LolketingCompose")
    }
}