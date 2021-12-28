package com.dsy.mvvm.utils

import splitties.init.appCtx

val Int.dp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), appCtx.resources.displayMetrics
    ).toInt()

val Int.sp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this.toFloat(), appCtx.resources.displayMetrics
    ).toInt()

val Int.hexString: String
    get() = Integer.toHexString(this)

fun Int.toBoolean():Boolean{
    return this == 1
}