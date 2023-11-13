package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.SearchAddressResult
import com.ezen.lolketing.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    fun selectAddress(keyword: String, isSearch: Boolean) {
        if (isSearch) {
            currentPage = 1
            currentKeyword = keyword
        }

        repository
            .fetchAddress(keyword = currentKeyword, currentPage = currentPage)
            .onEach {
                if (it.list.isNullOrEmpty()) {
                    event(Event.Error("입력한 주소를 확인해주세요"))
                } else {
                    event(Event.AddressSearchSuccess(it.list))
                    currentPage++
                    _isMoreData = it.isMoreData
                }
            }
            .catch {
                it.printStackTrace()
                event(Event.Error("입력한 주소를 확인해주세요"))
            }
            .launchIn(viewModelScope)

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