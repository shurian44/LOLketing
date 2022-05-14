package com.ezen.lolketing.view.gallery

import android.app.Application
import android.content.Context
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.GalleryUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
) : BaseViewModel<GalleryViewModel.Event>() {

    fun getImageList(application: Application) {
        event(Event.ShowGalleryList(GalleryUtil(application).getImageList()))
    }

    sealed class Event {
        data class ShowGalleryList(
            val list : List<GalleryItem>
        ) : Event()
    }

}