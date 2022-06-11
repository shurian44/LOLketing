package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.SearchAddressResult
import com.ezen.lolketing.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val repository: AddressRepository
) : BaseViewModel<AddressViewModel.Event>() {

    private var currentPage = 1
    private var currentKeyword = ""
    private var _isMoreData = true
    val isMoreData : Boolean
        get() = _isMoreData

    fun selectAddress(keyword: String, isSearch: Boolean) = viewModelScope.launch {
        if (isSearch) {
            currentPage = 1
            currentKeyword = keyword
        }

        repository.selectAddress(
            keyword = currentKeyword,
            currentPage = currentPage,
            successListener = {
                if (it.list == null || it.list.isEmpty()) {
                    event(Event.Error("입력한 주소를 확인해주세요"))
                } else {
                    event(Event.AddressSearchSuccess(it.list))
                    currentPage++
                    _isMoreData = it.isMoreData
                }
            },
            failureListener = {

            }
        )
    }

    sealed class Event {
        data class AddressSearchSuccess(
            val list : List<SearchAddressResult>,
        ) : Event()
        data class Error(
            val msg: String
        ) : Event()
    }

}