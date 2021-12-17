package com.dsy.mvvm.manager

import android.content.Context
import androidx.core.content.edit
import splitties.init.appCtx

object AppConfig {
    private val config = appCtx.getSharedPreferences("appConfig", Context.MODE_PRIVATE)


    /**
     * 按页朗读
     */
    var readAloudByPage: Boolean
        get() = config.getBoolean("readAloudByPage",false)
        set(value) {
            config.edit {
                putBoolean("readAloudByPage", value)
            }
        }

    /**
     * 亮屏时间
     */
    var keepLight: String?
        get() = config.getString("keepLight",null)
        set(value) {
            config.edit {
                putString("keepLight", value)
            }
        }

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