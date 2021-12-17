package com.dsy.mvvm.manager

import android.content.Context
import androidx.core.content.edit
import splitties.init.appCtx

object AppConfig {
    private val config = appCtx.getSharedPreferences("appConfig", Context.MODE_PRIVATE)


    /**
     * 弹幕开关
     */
    var isDanMuOpen: Boolean
        get() = config.getBoolean("isDanMuOpen",false)
        set(value) {
            config.edit {
                putBoolean("isDanMuOpen", value)
            }
        }

}