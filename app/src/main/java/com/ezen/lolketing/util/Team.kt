package com.ezen.lolketing.util

import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.ezen.lolketing.R

enum class Team(val teamName: String, @DrawableRes val image: Int) {
    T1("T1", R.drawable.logo_t1),
    GRIFFIN("GRIFFIN", R.drawable.logo_griffin),
    GENG("Gen.g Esports", R.drawable.icon_geng),
    DRAGON("DragonX", R.drawable.icon_dragonx),
    AF("Afreeca Freecs", R.drawable.icon_afreeca),
    SANDBOX("SANDBOX Gaming", R.drawable.icon_sandbox),
    DAMWON("DAMWON Gaming", R.drawable.icon_damwon),
    APK("APK Prince", R.drawable.icon_apk_prince),
    KT("kt Rolster", R.drawable.icon_rolster),
    HANWHA("Hanwha Life Esports", R.drawable.icon_hanwha);
}

fun setTeamLogoImageView(imageView : ImageView, team : String) {
    val res = Team.values().find { it.teamName.uppercase() == team.uppercase() }?.image ?: Team.T1.image
    imageView.setImageResource(res)
}

fun getRandomGame() : String {
    val teamList = Team.values().toMutableList().shuffled()
    return "${teamList[0].teamName}:${teamList[1].teamName}"
}

fun getBoardImage(teamName: String) : Int {

    return when (teamName) {
        Team.T1.teamName -> R.drawable.img_board_t1
        Team.GRIFFIN.teamName -> R.drawable.img_board_griffin
        Team.GENG.teamName -> R.drawable.img_board_geng
        Team.DRAGON.teamName -> R.drawable.img_board_dragon
        Team.AF.teamName -> R.drawable.img_board_af
        Team.SANDBOX.teamName -> R.drawable.img_board_sandbox
        Team.DAMWON.teamName -> R.drawable.img_board_damwon
        Team.APK.teamName -> R.drawable.img_board_apk
        Team.KT.teamName -> R.drawable.img_board_kt
        Team.HANWHA.teamName -> R.drawable.img_board_hanwha
        else -> R.drawable.img_board_t1
    }
}
