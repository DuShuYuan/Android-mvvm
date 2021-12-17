package com.dsy.mvvm.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.view.View
import android.view.View.*
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlin.math.abs


/**
 * 最近一次点击的时间
 */
private var lastClickTime: Long = 0

/**
 * 两次点击按钮之间的点击间隔不能少于1000毫秒
 */
private const val MIN_CLICK_DELAY_TIME = 1000

/**
 * 是否是快速点击
 */
val isFastClick: Boolean
    get() {
    val time = System.currentTimeMillis()
    val timeInterval = abs(time - lastClickTime)
    return if (timeInterval < MIN_CLICK_DELAY_TIME) {
        true
    } else {
        lastClickTime = time
        false
    }
}

inline fun View.onSingleClick(crossinline block: () -> Unit) = setOnClickListener {
    if (!isFastClick){
        block()
    }
}

private tailrec fun getCompatActivity(context: Context?): AppCompatActivity? {
    return when (context) {
        is AppCompatActivity -> context
        is androidx.appcompat.view.ContextThemeWrapper -> getCompatActivity(context.baseContext)
        is android.view.ContextThemeWrapper -> getCompatActivity(context.baseContext)
        else -> null
    }
}

val View.activity: AppCompatActivity?
    get() = getCompatActivity(context)

fun View.disableAutoFill() = run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.importantForAutofill = IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
    }
}

fun View.gone() {
    if (visibility != GONE) {
        visibility = GONE
    }
}

fun View.invisible() {
    if (visibility != INVISIBLE) {
        visibility = INVISIBLE
    }
}

fun View.visible() {
    if (visibility != VISIBLE) {
        visibility = VISIBLE
    }
}

fun View.screenshot(): Bitmap? {
    return runCatching {
        val screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(screenshot)
        c.translate(-scrollX.toFloat(), -scrollY.toFloat())
        draw(c)
        screenshot
    }.getOrNull()
}


fun RadioGroup.getIndexById(id: Int): Int {
    for (i in 0 until this.childCount) {
        if (id == get(i).id) {
            return i
        }
    }
    return 0
}

