package com.dsy.mvvm.widget.status

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.dsy.mvvm.databinding.LayoutStatusBinding
import com.dsy.mvvm.utils.inflateBindingWithGeneric
import splitties.views.onClick

@SuppressLint("ViewConstructor")
class StatusView(context: Context, private var retryTask: Runnable? = null) : LinearLayout(context) {

    val binding:LayoutStatusBinding by lazy {
        inflateBindingWithGeneric(this)
    }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
//        inflate(context, R.layout.layout_status, this)
//        setBackgroundColor(context.getColor2(R.color.col_background))
        binding.stateBtn.onClick {
            retryTask?.run()
        }
    }


    private var currentState = LoadStatus.SUCCESS

    fun setStatus(status: LoadStatus) {
        this.currentState = status
        var show = true
        when (status) {
            LoadStatus.SUCCESS -> {
                show = false
            }
            LoadStatus.LOADING -> {
                binding.stateLoading.isVisible = true
                binding.stateImg.isVisible = false
                binding.stateText.isVisible = false
                binding.stateBtn.isVisible = false
            }
            else -> {
                binding.stateLoading.isVisible = false
                binding.stateImg.isVisible = true
                binding.stateText.isVisible = true
                binding.stateImg.setImageResource(status.emptyImg)
                binding.stateText.setText(status.emptyTxt)
                if (status.emptyBtn != 0) {
                    binding.stateBtn.isVisible = true
                    binding.stateBtn.setText(status.emptyBtn)
                } else {
                    binding.stateBtn.isVisible = false
                }
            }
        }
        isVisible = show
    }

}