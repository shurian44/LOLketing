package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogCouponBinding
import com.ezen.lolketing.util.backgroundTransparent
import com.ezen.lolketing.util.dialogResize
import com.ezen.network.model.Coupon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CouponDialog(
    onClick: (Int) -> Unit
) : DialogFragment() {

    private val binding: DialogCouponBinding by lazy { DialogCouponBinding.inflate(layoutInflater) }

    private val _item = MutableStateFlow(
        CouponDialogItem(availableAdapter = CouponAdapter(onClick))
    )
    val item: StateFlow<CouponDialogItem> = _item

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그 배경 투명화
        dialog?.backgroundTransparent()
        binding.dialog = this

    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogResize(dialog)
    }

    fun setAvailableList(availableList: List<Coupon>) {
        _item.update { it.copy(availableList = availableList) }
        if (::isResumed.invoke()) {
            binding.invalidateAll()
        }
    }

    fun setUsedList(usedList: List<Coupon>) {
        _item.update { it.copy(usedList = usedList) }
        if (::isResumed.invoke()) {
            binding.invalidateAll()
        }
    }

    fun statusChange(view: View) {
        val status = view.id == binding.txtAvailable.id

        if (_item.value.status != status) {
            _item.update { it.copy(status = status) }
            binding.invalidateAll()
        }
    }

}

data class CouponDialogItem(
    val availableList: List<Coupon> = listOf(),
    val usedList: List<Coupon> = listOf(),
    val status: Boolean = true,
    val availableAdapter: CouponAdapter = CouponAdapter {},
    val usedAdapter: CouponAdapter = CouponAdapter {},
)