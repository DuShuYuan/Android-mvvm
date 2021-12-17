package com.dsy.mvvm.widget.status

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.dsy.mvvm.utils.gone
import com.dsy.mvvm.utils.invisible
import com.dsy.mvvm.utils.visible

class StatusHolder private constructor(private val context: Context,val wrapper: ViewGroup) {

    companion object {
        fun wrap(activity: Activity): StatusHolder {
            val wrapper = activity.findViewById<ViewGroup>(android.R.id.content)
            return wrap(wrapper.getChildAt(0))
        }

        fun wrap(view: View): StatusHolder {
            val wrapper = FrameLayout(view.context)
            val lp = view.layoutParams
            if (lp != null) {
                wrapper.layoutParams = lp
            }
            if (view.parent != null) {
                val parent = view.parent as ViewGroup
                val index = parent.indexOfChild(view)
                parent.removeView(view)
                parent.addView(wrapper, index)
            }
            val newLp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            wrapper.addView(view, newLp)
            return StatusHolder(view.context, wrapper)
        }
    }
    private var mRetryTask: Runnable? = null
    private var mCurStatusView: View? = null
    private val successView:View = wrapper.getChildAt(0)
    var curState = LoadStatus.SUCCESS
        private set


    fun withRetry(task: Runnable?): StatusHolder {
        mRetryTask = task
        return this
    }

    fun showLoading() {
        showLoadingStatus(LoadStatus.LOADING)
    }

    fun showLoadSuccess() {
        showLoadingStatus(LoadStatus.SUCCESS)
    }

    fun showLoadFailed() {
        showLoadingStatus(LoadStatus.ERROR)
    }

    fun showEmpty() {
        showLoadingStatus(LoadStatus.EMPTY)
    }

    private fun getView(convertView: View?, status: LoadStatus): StatusView {
        val sView: StatusView = if (convertView != null && convertView is StatusView) {
            convertView
        } else {
            StatusView(context, mRetryTask)
        }
        sView.setStatus(status)
        return sView
    }

    fun showLoadingStatus(status: LoadStatus) {
        if (curState == status) {
            return
        }
        curState = status
        if (curState == LoadStatus.SUCCESS){
            successView.visible()
            mCurStatusView?.gone()
            successView.bringToFront()
            return
        }else{
            successView.invisible()
        }
        try {
            val view: View = getView(mCurStatusView, status)
            if (view !== mCurStatusView || wrapper.indexOfChild(view) < 0) {
                if (mCurStatusView != null) {
                    wrapper.removeView(mCurStatusView)
                }
                wrapper.addView(view)
                val lp = view.layoutParams
                if (lp != null) {
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
            } else if (wrapper.indexOfChild(view) != wrapper.childCount - 1) {
                // 确保加载状态视图在前面
                view.bringToFront()
            }
            mCurStatusView?.gone()
            mCurStatusView = view
            mCurStatusView?.visible()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}