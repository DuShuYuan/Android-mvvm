package com.dsy.mvvm.base.dialog

import android.content.Context
import com.dsy.mvvm.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener

/**
 * @Description: 弹窗
 * @Author: DuShuYuan
 * @Date: 2020-08-13 18:40:47
 */
class ConfirmDialog(val context: Context, private val config: ConfirmDialog.() -> Unit) {
    var title: String? = null
    var message: String = ""
    var confirmText: String = context.getString(R.string.confirm)
    var cancelText: String = context.getString(R.string.cancel)
    var isHideCancel: Boolean = false
    var onConfirmClick: (() -> Unit)? = null
    var onCancelClick: (() -> Unit)? = null
    private var cancelListener: OnCancelListener? = null
    private var confirmListener: OnConfirmListener? = null

    init {
        config.invoke(this)
        onCancelClick?.let {
            cancelListener = OnCancelListener {
                it.invoke()
            }
        }
        onConfirmClick?.let {
            confirmListener = OnConfirmListener {
                it.invoke()
            }
        }
    }
    /**
     * 显示弹窗
     */
    fun show() {
        XPopup.Builder(context)
            .shareConfig()
            .asConfirm(
                title,
                message,
                cancelText,
                confirmText,
                confirmListener,
                cancelListener,
                isHideCancel,
                R.layout.dialog_confirm
            )
            .show()
    }
}
