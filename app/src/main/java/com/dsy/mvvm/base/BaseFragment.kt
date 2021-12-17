package com.dsy.mvvm.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.dsy.mvvm.utils.inflateBindingWithGeneric
import com.dsy.mvvm.widget.status.LoadStatus
import com.dsy.mvvm.widget.status.StatusHolder

abstract class VMBaseFragment<VB : ViewBinding, VM : ViewModel> : BaseFragment<VB>() {

    protected abstract val mViewModel: VM

}

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected val TAG: String = this.javaClass.simpleName
    private lateinit var mStatusHolder: StatusHolder

    private var _binding: VB? = null
    val mBinding: VB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBindingWithGeneric(inflater, container, false)

        return if (getStatusWrapView() == null) {
            mStatusHolder = StatusHolder.wrap(mBinding.root).withRetry { onReload() }
            mStatusHolder.wrapper
        } else {
            mStatusHolder = StatusHolder.wrap(getStatusWrapView()!!).withRetry { onReload() }
            mBinding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        MLog.d(TAG, "onViewCreated")
        onFragmentCreated(view, savedInstanceState)
        bindEvent()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        MLog.d(TAG, "onDestroyView")
        _binding = null
    }

    abstract fun onFragmentCreated(view: View, savedInstanceState: Bundle?)
    abstract fun bindEvent()
    abstract fun initData()

    /**
     * 状态布局包裹的view
     */
    protected open fun getStatusWrapView(): View? {
        return null
    }


    val mContext: Context
        get() = requireActivity()

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