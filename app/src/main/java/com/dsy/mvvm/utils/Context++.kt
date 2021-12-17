package com.dsy.mvvm.utils

import android.content.*
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.io.File

fun Context.getCompatColor(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun Context.getCompatDrawable(@DrawableRes id:Int):Drawable{
    return ContextCompat.getDrawable(this,id)!!
}

/**
 * 系统息屏时间
 */
val Context.sysScreenOffTime: Int
    get() {
        var screenOffTime = 0
        try {
            screenOffTime =
                Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return screenOffTime
    }

//val Context.statusBarHeight: Int
//    get() {
//        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
//        return resources.getDimensionPixelSize(resourceId)
//    }


fun Context.share(text: String, title: String) {
    kotlin.runCatching {
        val intent = Intent(Intent.ACTION_SEND)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, title))
    }
}


fun Context.getClipText(): String? {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    clipboard?.primaryClip?.let {
        if (it.itemCount > 0) {
            return it.getItemAt(0).text.toString().trim()
        }
    }
    return null
}

fun Context.sendMail(mail: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$mail")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: Exception) {
        toast(e.localizedMessage ?: "Error")
    }
}

/**
 * 系统是否暗色主题
 */
fun Context.sysIsDarkMode(): Boolean {
    val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return mode == Configuration.UI_MODE_NIGHT_YES
}

/**
 * 获取电量
 */
val Context.sysBattery: Int
    get() {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, iFilter)
        return batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    }

val Context.externalFilesDir: File
    get() = this.getExternalFilesDir(null) ?: this.filesDir

val Context.eCacheDir: File
    get() = this.externalCacheDir ?: this.cacheDir

fun Context.openUrl(url: String) {
    openUrl(Uri.parse(url))
}

fun Context.openUrl(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (intent.resolveActivity(packageManager) != null) {
        try {
            startActivity(intent)
        } catch (e: Exception) {
            toast(e.localizedMessage ?: "open url error")
        }
    } else {
        try {
            startActivity(Intent.createChooser(intent, "请选择浏览器"))
        } catch (e: Exception) {
            toast(e.localizedMessage ?: "open url error")
        }
    }
}


/**
 * 跳转到通知设置界面
 */
fun Context.toNotificationSetting() {
    try {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, applicationInfo.uid)
        } else {
            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", packageName)
            intent.putExtra("app_uid", applicationInfo.uid)
        }
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        // 出现异常则跳转到应用设置界面
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}

/**
 * 跳转 google play 商店
 */
fun Context.toAppStore(applicationId:String) {
    try {
        val uri = Uri.parse("market://details?id=$applicationId")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.android.vending")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}