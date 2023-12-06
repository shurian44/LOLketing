package com.ezen.lolketing.view.gallery

import android.app.Application
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.GalleryUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<GalleryItem>())
    val list: StateFlow<List<GalleryItem>> = _list

    private val _isDetail = MutableStateFlow(false)
    val isDetail: StateFlow<Boolean> = _isDetail

    val selectUri
        get() = _list.value.firstOrNull{ it.isChecked }?.contentUri

    fun fetchImageList(application: Application) {
        _list.value = GalleryUtil(application).getImageList()
    }

    fun onSelectChanged(selectIndex: Int) {
        _list.update {
            it.mapIndexed { index, galleryItem ->
                if (index == selectIndex) {
                    galleryItem.copy(isChecked = it[index].isChecked.not())
                } else {
                    galleryItem.copy(isChecked = false)
                }
            }
        }
    }

    fun updateIsDetail(value: Boolean) {
        _isDetail.value = value
    }

}