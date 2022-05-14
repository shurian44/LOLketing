package com.ezen.lolketing.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GalleryItem (
    val id : Long,
    val displayName : String,
    val dateTaken : Date,
    val contentUri : Uri,
    var isChecked : Boolean = false
) : Parcelable