package com.ezen.lolketing.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.InputFilter
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import okhttp3.internal.format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

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

fun <T> View.startActivityWithButton(clazz: Class<T>, activity: Activity) {
    activity.startActivity(clazz)
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
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

fun getCurrentDateTime(): Date =
    Calendar.getInstance().time

fun getSimpleDateFormatMillSec(date: String, format : String = "yyyy.MM.dd HH:mm") : Long? {
    val timeFormat = SimpleDateFormat(format, Locale.KOREA)
    return timeFormat.parse(date)?.time
}

fun densityDp(context: Context, size: Int) : Int {
    val dm = context.resources.displayMetrics
    return (size * dm.density).roundToInt()
}

fun dpToPx(context: Context, size: Int) : Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, size.toFloat(), context.resources.displayMetrics
    ).roundToInt()
}

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

// 쿠폰 유효기간 계산 : 60일
fun getCouponValidityPeriod() : String {
    val lateDay60 = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 60L)
    return lateDay60.timestampToString()
}

fun getShoppingCategoryList() = listOf(
    Code.SHOP_ALL.codeName, Code.STATUE.codeName, Code.FIGURE.codeName,
    Code.ACCESSORY.codeName, Code.DOLL.codeName, Code.T_SHIRT.codeName,
    Code.JACKET.codeName, Code.PAJAMAS.codeName, Code.ART.codeName, Code.BOARD_GAME.codeName
)