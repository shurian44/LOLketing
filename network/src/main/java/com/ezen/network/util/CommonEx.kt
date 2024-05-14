package com.ezen.network.util

import java.text.DecimalFormat

fun Long.addCommas(): String = DecimalFormat("###,###").format(this)

fun Int.addCommas(): String = DecimalFormat("###,###").format(this)

fun Long.priceFormat() = addCommas().plus("원")

fun Int.priceFormat() = addCommas().plus("원")