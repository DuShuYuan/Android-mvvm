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
     * 自动购买
     */
    var isAutoPay: Boolean
        get() = userConfig.getBoolean("isAutoPay",false)
        set(value) {
            userConfig.edit {
                putBoolean("isAutoPay", value)
            }
        }
    /**
     * 优先使用阅读券
     */
    var isReadingVoucherFirst: Boolean
        get() = userConfig.getBoolean("isReadingVoucherFirst",false)
        set(value) {
            userConfig.edit {
                putBoolean("isReadingVoucherFirst", value)
            }
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

    /**
     * 是否展示vip续费弹窗提示
     */
    var isShowVipRenew: Boolean
        get() = userConfig.getBoolean("isShowVipRenew",true)
        set(value) {
            userConfig.edit {
                putBoolean("isShowVipRenew", value)
            }
        }

    /**
     * 是否展示vip续费充值弹窗提示
     */
    var isShowVipRenewRecharge: Boolean
        get() = userConfig.getBoolean("isShowVipRenewRecharge",true)
        set(value) {
            userConfig.edit {
                putBoolean("isShowVipRenewRecharge", value)
            }
        }
}