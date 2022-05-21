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

    fun selectAddress(keyword: String, isSearch: Boolean) = viewModelScope.launch {
        if (isSearch) {
            currentPage = 1
            currentKeyword = keyword
        }
        val result = repository.selectAddress(
            keyword = currentKeyword,
            currentPage = currentPage
        )
        if (result?.list.isNullOrEmpty()) {
            event(Event.Error("입력한 주소를 확인해주세요"))
        } else {
            event(Event.AddressSearchSuccess(result!!.list!!, result.isMoreData))
            currentPage++
        }
    }

    sealed class Event {
        data class AddressSearchSuccess(
            val list : List<SearchAddressResult>,
            val isMoreData: Boolean
        ) : Event()
        data class Error(
            val msg: String
        ) : Event()
    }

}