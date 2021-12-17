package com.dsy.mvvm.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.dsy.mvvm.R
import com.dsy.mvvm.utils.ATH
import com.dsy.mvvm.utils.getCompatColor
import com.dsy.mvvm.utils.inflateBindingWithGeneric
import com.dsy.mvvm.utils.logger
import com.dsy.mvvm.widget.TitleBarHelper
import com.dsy.mvvm.widget.status.LoadStatus
import com.dsy.mvvm.widget.status.StatusHolder

abstract class VMBaseActivity<VB : ViewBinding, VM : ViewModel> : BaseActivity<VB>() {
    protected abstract val mViewModel: VM

}

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName

    val mContext: Context
        get() = this

    private val mStatusHolder: StatusHolder by lazy {
        if (getStatusWrapView() != null) {
            StatusHolder.wrap(getStatusWrapView()!!)
        } else {
            StatusHolder.wrap(this)
        }
    }
    val mTitleBarHelper by lazy {
        TitleBarHelper(this)
    }
    protected val mBinding: VB by lazy { inflateBindingWithGeneric(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.dTag(TAG, "$TAG.onCreate() ================")

        setContentView(mBinding.root)

        ATH.setStatusBarColorAuto(this, getCompatColor(R.color.col_bg_title_bar))
        ATH.setNavigationBarColorAuto(this, getCompatColor(R.color.col_bg_title_bar))

        mStatusHolder.withRetry { onReload() }
        onActivityCreated(savedInstanceState)
        bindEvent()
        initData()
    }

    /**
     * 控件绑定
     */
    protected abstract fun onActivityCreated(savedInstanceState: Bundle?)

    /**
     * 事件触发绑定
     */
    protected abstract fun bindEvent()

    /**
     * 数据初始化
     */
    protected abstract fun initData()

    override fun onDestroy() {
        logger.dTag(TAG, "$TAG.onDestroy() ================")
        super.onDestroy()
    }

    /**
     * 状态布局包裹的view
     */
    protected open fun getStatusWrapView(): View? {
        return null
    }

    protected open fun onReload() {

    }

    fun showLoadingView() {
        mStatusHolder.showLoading()
    }

    fun showContentView() {
        mStatusHolder.showLoadSuccess()
    }

    fun showErrorView() {
        mStatusHolder.showLoadFailed()
    }

    fun showEmptyView() {
        mStatusHolder.showEmpty()
    }

    fun showCustomView(status: LoadStatus) {
        mStatusHolder.showLoadingStatus(status)
    }


}