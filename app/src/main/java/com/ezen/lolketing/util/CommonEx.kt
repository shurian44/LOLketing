package com.ezen.lolketing.util

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast

fun <T> Activity.startActivity(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}

fun <T> Activity.startActivity(clazz: Class<T>, vararg flags : Int) {
    startActivity(Intent(this, clazz).also { intent ->
        flags.forEach {
            intent.flags = it
        }
    })
}

fun <T> View.startActivityWithButton(clazz: Class<T>, activity: Activity) {
    activity.startActivity(clazz)
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}