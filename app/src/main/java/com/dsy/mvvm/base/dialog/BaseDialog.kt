package com.dsy.mvvm.base.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.core.CenterPopupView
import com.dsy.mvvm.utils.coroutine.Coroutine
import com.dsy.mvvm.utils.inflateBindingWithGeneric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import splitties.systemservices.layoutInflater
import kotlin.coroutines.CoroutineContext


fun XPopup.Builder.shareConfig(): XPopup.Builder {
    return this.isDestroyOnDismiss(true)
//        .isViewMode(true)
//        .isDarkTheme(SpManager.isNightModel)
//        .isLightStatusBar(!SpManager.isNightModel)
//        .isLightNavigationBar(!SpManager.isNightModel)
//        .navigationBarColor(
//            (ActivityManager.getTopActivity() ?: appCtx).getCompatColor(R.color.col_bg_title_bar)
//        )
}

@SuppressLint("ViewConstructor")
open class BaseBottomDialog<VB : ViewBinding>(context: Context) : BottomPopupView(context), CoroutineScope {

    val mBinding: VB by lazy {
        inflateBindingWithGeneric(layoutInflater)
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private lateinit var job: Job

    override fun addInnerContent() {
        job = Job()
        bottomPopupContainer.addView(mBinding.root)
        mBinding.root.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    }

    open fun showDialog() {
        XPopup.Builder(context)
            .shareConfig()
            .asCustom(this).show()
    }

    override fun onDismiss() {
        super.onDismiss()
        job.cancel()
    }

    fun <T> execute(
        scope: CoroutineScope = this,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> T
    ) = Coroutine.async(scope, context) { block() }

}

@SuppressLint("ViewConstructor")
open class BaseCenterDialog<VB : ViewBinding>(context: Context) : CenterPopupView(context) {

    val mBinding: VB by lazy {
        inflateBindingWithGeneric(layoutInflater)
    }

    override fun addInnerContent() {
        centerPopupContainer.addView(mBinding.root)
    }

    open fun showDialog() {
        XPopup.Builder(context)
            .shareConfig()
            .asCustom(this).show()
    }
}