package com.ezen.lolketing.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.CustomTicketBinding

class CustomTicketView : ConstraintLayout {

    private val binding = CustomTicketBinding.inflate(LayoutInflater.from(context))

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        initView()
    }

    private fun initView() {
        addView(binding.root)

        binding.root.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

    }

    fun setItemInfo(item: TicketItem) {
        binding.info = item
    }

}

data class TicketItem(
    val id: Int,
    val leftTeam: String,
    val rightTeam: String,
    val message: String,
    val status: String = POSSIBLE
) {
    fun getStatusColor() = when(status) {
        SOLD_OUT -> R.color.gray
        FINISH -> R.color.yellow
        else -> R.color.main_color
    }

    fun getStatusDotRes() = when(status) {
        SOLD_OUT -> R.drawable.ticket_hall_gray
        FINISH -> R.drawable.ticket_hall_yello
        else -> R.drawable.ticket_hall_main
    }

    companion object {
        const val POSSIBLE = "possible"
        const val FINISH = "finish"
        const val SOLD_OUT = "sold out"
    }
}