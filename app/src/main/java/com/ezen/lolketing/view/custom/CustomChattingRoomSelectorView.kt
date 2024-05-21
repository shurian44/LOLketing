package com.ezen.lolketing.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ezen.lolketing.databinding.CustomChattingRoomSelectorBinding
import com.ezen.network.model.ChattingRoomInfo

class CustomChattingRoomSelectorView : ConstraintLayout {

    private val binding = CustomChattingRoomSelectorBinding.inflate(LayoutInflater.from(context))
    private var onClickListener: (ChattingRoomInfo, String) -> Unit = { _, _ -> }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context, attributeSet, defStyle) {
        initView()
    }

    private fun initView() {
        addView(binding.root)

        binding.root.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    fun setInfo(info: ChattingRoomInfo) {
        binding.info = info
        binding.imgTeamLeft.setOnClickListener {
            onClickListener(info, info.leftTeam)
        }
        binding.imgTeamRight.setOnClickListener {
            onClickListener(info, info.rightTeam)
        }
    }

    fun setOnClickListener(onClick: (ChattingRoomInfo, String) -> Unit) {
        onClickListener = onClick
    }

}