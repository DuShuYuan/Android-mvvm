package com.dsy.mvvm.utils

import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.dsy.mvvm.BuildConfig
import com.dsy.mvvm.R
import splitties.init.appCtx
import java.io.File


val File.uri: Uri?
    get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            try {
                return FileProvider.getUriForFile(
                    appCtx,
                    appCtx.getString(R.string.fileProvider, BuildConfig.APPLICATION_ID),
                    this
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            return Uri.fromFile(this)
        }
        return null
    }