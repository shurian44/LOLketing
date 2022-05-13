package com.ezen.lolketing.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.ezen.lolketing.R

enum class Team(name: String, @DrawableRes val image: Int) {
    T1("T1", R.drawable.logo_t1),
    GRIFFIN("GRIFFIN", R.drawable.logo_griffin),
    GENG("Gen.g Esports", R.drawable.icon_geng),
    DRAGON("DragonX", R.drawable.icon_dragonx),
    AF("Afreeca Freecs", R.drawable.icon_afreeca),
    SANDBOX("SANDBOX Gamming", R.drawable.icon_sandbox),
    DAMWON("DAMWON Gamming", R.drawable.icon_damwon),
    APK("APK Prince", R.drawable.icon_apk_prince),
    KT("kt Rolster", R.drawable.icon_rolster),
    HANWHA("Hanwha Life Esports", R.drawable.icon_hanwha);
}

fun setTeamLogoImageView(imageView : ImageView, team : String) {
    imageView.setImageResource(Team.valueOf(team).image)
}