package com.dsy.mvvm.utils

import splitties.init.appCtx

val Float.dp: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, appCtx.resources.displayMetrics
    )

val Float.sp: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, appCtx.resources.displayMetrics
    )

