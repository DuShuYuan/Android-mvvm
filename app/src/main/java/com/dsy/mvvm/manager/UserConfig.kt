package com.dsy.mvvm.manager

import android.content.Context
import androidx.core.content.edit
import splitties.init.appCtx

object UserConfig {
    private val userConfig = appCtx.getSharedPreferences("userConfig", Context.MODE_PRIVATE)

    fun clear(){
        userConfig.edit { clear() }
    }

    /**
     * 弹幕开关
     */
    var isDanMuOpen: Boolean
        get() = userConfig.getBoolean("isDanMuOpen",false)
        set(value) {
            userConfig.edit {
                putBoolean("isDanMuOpen", value)
            }
        }

}