package com.ezen.lolketing.util

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.ezen.lolketing.model.GalleryItem
import java.util.*

class GalleryUtil(private val application: Application) {

    private val imageList = mutableListOf<GalleryItem>()

    fun getImageList() : List<GalleryItem> {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
        )

        val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val cursor = application.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = Date(cursor.getLong(dateTakenColumn))
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                imageList.add(
                    GalleryItem(
                        id = id,
                        dateTaken = dateTaken,
                        displayName = displayName,
                        contentUri = contentUri
                    )
                )
                Log.d("+++++", "id: $id, display_name: $displayName, date_taken: $dateTaken, content_uri: $contentUri")
            }
        }

        return imageList
    }

}