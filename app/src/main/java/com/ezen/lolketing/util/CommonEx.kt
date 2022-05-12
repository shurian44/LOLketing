package com.ezen.lolketing.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

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

fun <T> Activity.createIntent(clazz: Class<T>) =
    Intent(this, clazz)


fun <T> Activity.createIntent(clazz: Class<T>, vararg flags : Int) =
    Intent(this, clazz).also { intent ->
        flags.forEach {
            intent.flags = it
        }
    }

fun <T> View.startActivityWithButton(clazz: Class<T>, activity: Activity) {
    activity.startActivity(clazz)
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

inline fun <reified T>listToArrayList(list : List<T>?) : ArrayList<T>{
    val arrayList : ArrayList<T> = arrayListOf()
    list?.toTypedArray()?.let {
        arrayList.addAll(it)
    }
    return arrayList
}

fun setGlide(view: ImageView, uri: String) {
    Glide.with(view.context).load(uri).fitCenter().into(view)
}

fun setGlide(view: ImageView, uri: Uri) {
    Glide.with(view.context).load(uri).fitCenter().into(view)
}

fun String.htmlFormat() : Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }