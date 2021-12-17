package com.dsy.mvvm.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dsy.mvvm.databinding.LayoutToolbarBinding
import com.dsy.mvvm.utils.visible
import splitties.systemservices.layoutInflater
import splitties.views.onClick

class TitleBar(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    val binding: LayoutToolbarBinding
    init {
        orientation = VERTICAL
        binding = LayoutToolbarBinding.inflate(layoutInflater,this,true)
    }
}

class TitleBarHelper(activity: Activity){
    private val parent: ViewGroup?
    private val titleBar:TitleBar
    init {
        val contentView: View = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        parent = contentView.parent as ViewGroup?
        titleBar = TitleBar(activity)
        titleBar.layoutParams = contentView.layoutParams
        parent?.removeView(contentView)
        titleBar.addView(contentView)
        parent?.addView(titleBar)

        titleBar.binding.tbIvLeft.onClick {
            onBack?.invoke() ?: activity.onBackPressed()
        }
    }

    val ivBack:ImageView
        get() = titleBar.binding.tbIvLeft
    val tvTitle:TextView
        get() = titleBar.binding.tbTvTitleName

    fun setTitle(title:String?){
        titleBar.binding.tbTvTitleName.text = title
    }
    fun setTitle(@StringRes title:Int){
        titleBar.binding.tbTvTitleName.setText(title)
    }
    fun setBackground(@ColorInt color:Int){
        titleBar.binding.layToolbar.setBackgroundColor(color)
    }

    fun setAction(@DrawableRes id:Int,onClick:(()->Unit)){
        titleBar.binding.tbIvRight.visible()
        titleBar.binding.tbIvRight.setImageResource(id)
        titleBar.binding.tbIvRight.onClick {
            onClick()
        }
    }
    fun setAction2(@DrawableRes id:Int,onClick:(()->Unit)){
        titleBar.binding.tbIvRight2.visible()
        titleBar.binding.tbIvRight2.setImageResource(id)
        titleBar.binding.tbIvRight2.onClick {
            onClick()
        }
    }
    var onBack:(()->Unit)? = null
}