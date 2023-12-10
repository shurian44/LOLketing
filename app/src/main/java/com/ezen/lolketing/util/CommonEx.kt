package com.ezen.lolketing.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Handler
import android.text.Html
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Calendar
import java.util.Date


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

fun <T> Context.startActivity(clazz: Class<T>, vararg flags : Int) {
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

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.showErrorMessageAndFinish() {
    toast("오류가 발생하였습니다")
    Handler(mainLooper).postDelayed({ finish() }, 1000)
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(msgRes: Int) {
    Toast.makeText(this, getString(msgRes), Toast.LENGTH_SHORT).show()
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun Fragment.toast(msg: String) {
    requireActivity().toast(msg)
}

fun setGlide(view: ImageView, uri: String) {
    Glide.with(view.context).load(uri).fitCenter().into(view)
}

fun setGlide(view: ImageView, uri: Uri) {
    Glide.with(view.context).load(uri).fitCenter().into(view)
}

fun String.htmlFormat() : Spanned =
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)

fun getCurrentDateTime(): Date =
    Calendar.getInstance().time

fun setSpecialCharacterRestrictions() = InputFilter { source, _, _, _, _, _ ->
    source.forEach {
        if (!Character.isLetterOrDigit(it)) {
            return@InputFilter ""
        }
    }
    null
}

fun Dialog.backgroundTransparent() {
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

fun getShoppingCategoryList() = listOf(
    Code.SHOP_ALL.codeName, Code.STATUE.codeName, Code.FIGURE.codeName,
    Code.ACCESSORY.codeName, Code.DOLL.codeName, Code.T_SHIRT.codeName,
    Code.JACKET.codeName, Code.PAJAMAS.codeName, Code.ART.codeName, Code.BOARD_GAME.codeName
)

fun RecyclerView.addOnScrollListener(onScrolled: (RecyclerView) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrolled(recyclerView)
        }
    })
}

fun Context.dpToPx(dpSize: Int): Int =
    (dpSize * resources.displayMetrics.density).toInt()