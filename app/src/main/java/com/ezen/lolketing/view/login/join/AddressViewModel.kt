package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.AddressSearchInfo
import com.ezen.lolketing.model.SearchAddressResult
import com.ezen.lolketing.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val repository: AddressRepository
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<SearchAddressResult>())
    val list: StateFlow<List<SearchAddressResult>> = _list

    private val _info = MutableStateFlow(AddressSearchInfo())
    val info: StateFlow<AddressSearchInfo> = _info

    private var beforeKeyword = ""
    private var currentPage = 1
    private var isMoreData = true

    private fun setIsSearchMode(isSearchMode: Boolean) {
        _info.update { it.copy(isSearchMode = isSearchMode) }
    }

    fun setAddress(address: String) {
        _info.update { it.copy(address = address, keyword = address) }
        setIsSearchMode(false)
    }

    fun fetchAddressList() {
        if (_info.value.isSearchMode.not()) setIsSearchMode(true)

        if (_info.value.keyword != beforeKeyword) {
            currentPage = 1
            _list.value = listOf()
            beforeKeyword = _info.value.keyword
            isMoreData = true
        }

        if (isMoreData.not()) return

        repository
            .fetchAddress(keyword = _info.value.keyword, currentPage = currentPage)
            .onEach {
                if (it.list.isNullOrEmpty()) {
                    isMoreData = false
                    if (_list.value.isEmpty()) {
                        updateMessage("입력한 주소를 확인해주세요")
                    }
                } else {
                    currentPage++
                    _list.value = _list.value.toMutableList().also { list -> list.addAll(it.list) }
                    isMoreData = it.isMoreData
                }
            }
            .catch {
                it.printStackTrace()
                updateMessage("입력한 주소를 확인해주세요")
            }
            .launchIn(viewModelScope)
    }
}