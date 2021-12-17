package com.dsy.mvvm.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.graphics.ColorUtils
import com.dsy.mvvm.R

object ATH {

    private const val KEY_OFFSET = -123

    fun fullScreen(activity: Activity, isLight: Boolean, offsetView: View? = null) {
        fullScreen(activity)
        offsetView?.let { addMarginTopEqualStatusBarHeight(it) }
        setStatusBarColor(activity, Color.TRANSPARENT)
        setLightStatusBar(activity, isLight)
        setNavigationBarColorAuto(activity,activity.getCompatColor(R.color.col_bg_title_bar))
    }

    fun fullScreen(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.setDecorFitsSystemWindows(true)
        }
        fullScreenOld(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    @Suppress("DEPRECATION")
    private fun fullScreenOld(activity: Activity) {
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        activity.window.clearFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
    }

    fun addMarginTopEqualStatusBarHeight(view: View) {
        val haveSetOffset = view.getTag(KEY_OFFSET)
        if (haveSetOffset != null && haveSetOffset as Boolean) return
        val layoutParams = view.layoutParams as MarginLayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin + StatusBarHeight,
            layoutParams.rightMargin,
            layoutParams.bottomMargin
        )
        view.setTag(KEY_OFFSET, true)
    }

    fun setStatusBarColorAuto(activity: Activity, color: Int) {
        setStatusBarColor(activity, color)
        setLightStatusBar(activity, ColorUtils.calculateLuminance(color) >= 0.5)
    }

    fun setStatusBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
        }
    }

    fun setLightStatusBar(activity: Activity, enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.let {
                if (enabled) {
                    it.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    it.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
            }
        }
        setLightStatusBarOld(activity, enabled)
    }

    @Suppress("DEPRECATION")
    private fun setLightStatusBarOld(activity: Activity, enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            val systemUiVisibility = decorView.systemUiVisibility
            if (enabled) {
                decorView.systemUiVisibility =
                    systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility =
                    systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }

    fun setNavigationBarColorAuto(activity: Activity, color: Int) {
        setNavigationBarColor(activity, color)
        setLightNavigationBar(activity, ColorUtils.calculateLuminance(color) >= 0.5)
    }

    fun setNavigationBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.navigationBarColor = color
        }
    }

    fun setLightNavigationBar(activity: Activity, enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.let {
                if (enabled) {
                    it.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                } else {
                    it.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }
            }
        }
        setLightNavigationBarOld(activity, enabled)
    }

    @Suppress("DEPRECATION")
    private fun setLightNavigationBarOld(activity: Activity, enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val decorView = activity.window.decorView
            var systemUiVisibility = decorView.systemUiVisibility
            systemUiVisibility = if (enabled) {
                systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            decorView.systemUiVisibility = systemUiVisibility
        }
    }

    /**
     * 更新状态栏,导航栏
     */
    fun upSystemUiVisibility(activity: Activity, toolBarHide: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.let {
                if (toolBarHide) {
                    it.hide(WindowInsets.Type.statusBars())
                    it.hide(WindowInsets.Type.navigationBars())
                } else {
                    it.show(WindowInsets.Type.statusBars())
                    it.show(WindowInsets.Type.navigationBars())
                }
            }
        }
        upSystemUiVisibilityO(activity, toolBarHide)
        setLightStatusBar(activity, false)
        setStatusBarColor(activity, Color.TRANSPARENT)
    }

    @Suppress("DEPRECATION")
    private fun upSystemUiVisibilityO(activity: Activity, toolBarHide: Boolean = true) {
        var flag = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        if (toolBarHide) {
            flag = flag or View.SYSTEM_UI_FLAG_FULLSCREEN
            flag = flag or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        activity.window.decorView.systemUiVisibility = flag
    }
}