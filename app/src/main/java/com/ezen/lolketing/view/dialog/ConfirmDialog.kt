package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.DialogConfrimBinding
import com.ezen.lolketing.util.backgroundTransparent
import com.ezen.lolketing.util.dialogResize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConfirmDialog(
    dialogItem: CommonDialogItem
) : DialogFragment() {

    private val binding: DialogConfrimBinding by lazy { DialogConfrimBinding.inflate(layoutInflater) }
    val item: StateFlow<CommonDialogItem> = MutableStateFlow(dialogItem)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그 배경 투명화
        dialog?.backgroundTransparent()
        isCancelable = item.value.cancelable
        binding.dialog = this
    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogResize(dialog)
    }

    fun onOkClick() {
        item.value.onOkClick.invoke()
        dismiss()
    }

    fun onCancelClick() {
        item.value.onCancelClick.invoke()
        dismiss()
    }

}

data class CommonDialogItem(
    val title: String = "",
    val message: String = "",
    val okText: String = "확인",
    val cancelText: String = "취소",
    val onOkClick: () -> Unit = {},
    val onCancelClick: () -> Unit = {},
    @ColorRes
    val okButtonColor: Int = R.color.main_color,
    val cancelable: Boolean = false
)