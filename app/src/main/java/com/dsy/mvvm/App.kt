package com.dsy.mvvm

import android.app.Activity
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import com.bumptech.glide.Glide
import com.dsy.mvvm.manager.ActivityManager
import com.dsy.mvvm.utils.CrashHandler
import com.dsy.mvvm.utils.GSON
import com.dsy.mvvm.utils.log.LogUtils
import com.dsy.mvvm.utils.logger
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.ScreenUtils

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        //崩溃捕获
        CrashHandler.getInstance().init(this)

        initConfig()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    private fun initConfig(){

        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG)

        LiveEventBus.config()
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false)
        registerActivityLifecycleCallbacks(ActivityManager)

        initRefreshLayout()
        initAutoSize()

        logger.json(GSON.toJson(resources.displayMetrics))
    }

    private fun initRefreshLayout() {

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            return@setDefaultRefreshHeaderCreator MaterialHeader(context)
//                .setColorSchemeResources(R.color.col_main)
//                .setProgressBackgroundColorSchemeResource(R.color.col_bg_refresh)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
            layout.setEnableFooterFollowWhenNoMoreData(false)
            return@setDefaultRefreshFooterCreator ClassicsFooter(context).setDrawableSize(20f)
                .setFinishDuration(0)
        }
    }

    private fun initAutoSize(){

        AutoSizeConfig.getInstance().onAdaptListener = object : onAdaptListener {
            override fun onAdaptBefore(target: Any, activity: Activity) {
                //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
                if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    AutoSizeConfig.getInstance().screenWidth = ScreenUtils.getScreenSize(activity)[1]
                    AutoSizeConfig.getInstance().screenHeight = ScreenUtils.getScreenSize(activity)[0]
                } else {
                    AutoSizeConfig.getInstance().screenWidth = ScreenUtils.getScreenSize(activity)[0]
                    AutoSizeConfig.getInstance().screenHeight = ScreenUtils.getScreenSize(activity)[1]
                }
            }

            override fun onAdaptAfter(target: Any, activity: Activity) {

            }
        }
    }
}