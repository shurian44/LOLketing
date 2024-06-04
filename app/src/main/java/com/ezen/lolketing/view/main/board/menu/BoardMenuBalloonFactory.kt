package com.ezen.lolketing.view.main.board.menu

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.ezen.lolketing.R
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec

class BoardMenuBalloonFactory: Balloon.Factory() {

    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {
        return Balloon.Builder(context)
            .setLayout(R.layout.layout_board_menu)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setArrowOrientation(ArrowOrientation.TOP)
            .setArrowOrientation(ArrowOrientation.TOP)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowPosition(0.5f)
            .setArrowSize(10)
            .setTextSize(12f)
            .setCornerRadius(6f)
            .setBackgroundColorResource(R.color.light_black)
            .setBalloonAnimation(BalloonAnimation.FADE)
            .setDismissWhenShowAgain(true)
            .setDismissWhenTouchOutside(true)
            .setDismissWhenOverlayClicked(false)
            .setLifecycleOwner(lifecycle)
            .build()
    }
}