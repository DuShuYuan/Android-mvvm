package com.dsy.mvvm.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.text.TextUtils
import android.util.LayoutDirection
import android.view.WindowManager
import androidx.core.text.TextUtilsCompat
import splitties.init.appCtx
import java.util.*

val isRtl:Boolean get() = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL

val ScreenWidth: Int
    get() {
//        return appCtx.resources.displayMetrics.widthPixels
        val wm = appCtx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getRealSize(point)
        return point.x
    }

val ScreenHeight: Int
    get() {
//        return appCtx.resources.displayMetrics.heightPixels
        val wm = appCtx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getRealSize(point)
        return point.y
    }

/**
 * 状态栏高度
 */
val StatusBarHeight: Int by lazy {
    val resources = appCtx.resources
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    resources.getDimensionPixelSize(resourceId)
}

/**
 * 虚拟按键的高度
 */
val NavigationBarHeight: Int
    get() {
        var height = 0
        val rs = appCtx.resources
        val id = rs.getIdentifier("navigation_bar_height", "dimen", "android")
        if (id != 0) {
            height = rs.getDimensionPixelSize(id)
        }
        return height
    }

@SuppressLint("PrivateApi")
object ScreenUtils {

    /**
     * 是否有刘海屏
     *
     * @return
     */
    fun hasNotchInScreen(activity: Activity): Boolean {
        // 通过其他方式判断是否有刘海屏  目前官方提供有开发文档的就 小米，vivo，华为（荣耀），oppo
        val manufacturer = Build.MANUFACTURER
        if (TextUtils.isEmpty(manufacturer)) {
            return false
        } else if (manufacturer.equals("HUAWEI", ignoreCase = true)) {
            return hasNotchHw(activity)
        } else if (manufacturer.equals("xiaomi", ignoreCase = true)) {
            return hasNotchXiaoMi()
        } else if (manufacturer.equals("oppo", ignoreCase = true)) {
            return hasNotchOPPO()
        } else if (manufacturer.equals("vivo", ignoreCase = true)) {
            return hasNotchVIVO()
        } else if (Build.VERSION.SDK_INT >= 28) {
            // android  P 以上有标准 API 来判断是否有刘海屏
            val windowInsets = activity.window.decorView.rootWindowInsets
            if (windowInsets != null) {
                val displayCutout = windowInsets.displayCutout
                // 说明有刘海屏
                return displayCutout != null
            }
        }
        return false
    }

    /**
     * 判断vivo是否有刘海屏
     * https://swsdl.vivo.com.cn/appstore/developer/uploadfile/20180328/20180328152252602.pdf
     */
    private fun hasNotchVIVO(): Boolean {
        return try {
            val c = Class.forName("android.util.FtFeature")
            val get = c.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            get.invoke(c, 0x20) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断oppo是否有刘海屏
     * https://open.oppomobile.com/wiki/doc#id=10159
     */
    private fun hasNotchOPPO(): Boolean {
        return appCtx.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    /**
     * 判断xiaomi是否有刘海屏
     * https://dev.mi.com/console/doc/detail?pId=1293
     */
    private fun hasNotchXiaoMi(): Boolean {
        return try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            get.invoke(c, "ro.miui.notch", 1) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断华为是否有刘海屏
     * https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114
     */
    private fun hasNotchHw(activity: Activity): Boolean {
        return try {
            val cl = activity.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
            false
        }
    }
}