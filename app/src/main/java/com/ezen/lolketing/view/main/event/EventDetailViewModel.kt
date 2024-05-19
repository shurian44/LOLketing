package com.ezen.lolketing.view.main.event

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.EventDetailItem
import com.ezen.lolketing.repository.EventRepository
import com.ezen.lolketing.view.main.event.EventDetailActivity.Companion.NewUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
// 제거 예정
@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository
) : StatusViewModel() {

    private val _page = MutableStateFlow(NewUser)
    val page: StateFlow<Int> = _page

    private val _item = MutableStateFlow(EventDetailItem())
    val item: StateFlow<EventDetailItem> = _item

    fun setPage(page: Int) {
        _page.value = page

        if (page == NewUser) fetchNewUserInfo()
    }

    /** 신규 유저 화면 정보 조회 **/
    private fun fetchNewUserInfo() {
        repository
            .fetchNewUserInfo()
            .setLoadingState()
            .onEach { _item.value = it }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }


    /** 신규 가입 쿠폰 사용 및 포인트 적립 **/
    fun updateUseNewUserCoupon() {
        if (_item.value.isUsed.not()) {
            updateMessage("이미 사용한 쿠폰입니다.")
            return
        }

        repository
            .updateUseNewUserCoupon(_item.value.documentId)
            .setLoadingState()
            .onEach { updateMessage(it) }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }

}