package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseDialog
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.DialogCategorySelectBinding
import com.ezen.lolketing.model.Category
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.toast

class CategorySelectDialog(
    val data: String,
    val listener : (String) -> Unit
) : BaseDialog<DialogCategorySelectBinding>(R.layout.dialog_category_select) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        dialog = this@CategorySelectDialog
        recyclerView.adapter = CategorySelectAdapter().also {
            it.submitList(setCategoryList())
            setSelectItem(it, data)
        }
    }

    private fun setCategoryList() = listOf(
        Category(Code.FREE_BOARD.codeName),
        Category(Code.QUESTION_BOARD.codeName),
        Category(Code.GAME_BOARD.codeName),
    )

    private fun setSelectItem(adapter: CategorySelectAdapter, data: String) {
        if (data.isEmpty()) return
        adapter.setSelectItem(data)
    }

    fun onCancel(view: View) {
        dismiss()
    }

    fun onOkClick(view: View) {
        val result = (binding.recyclerView.adapter as? CategorySelectAdapter)?.getSelectInfo()

        if (result == null) {
            toast(getString(R.string.select_category))
            return
        }

        listener(result)
        dismiss()
    }
}