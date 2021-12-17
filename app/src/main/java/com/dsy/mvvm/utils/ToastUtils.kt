@file:Suppress("unused")

package com.dsy.mvvm.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

private var toast: Toast? = null

private fun show(context: Context, message: Int, duration: Int) {
    runOnUI {
        cancel()
        toast = Toast.makeText(context, message, duration)
        toast?.show()
    }
}

private fun show(context: Context, message: CharSequence?, duration: Int) {
    runOnUI {
        cancel()
        toast = Toast.makeText(context, message, duration)
        toast?.show()
    }
}

private fun cancel() {
    toast?.cancel()
    toast = null
}

fun Context.toast(message: Int) {
    show(this, message, Toast.LENGTH_SHORT)
}

fun Context.toast(message: CharSequence?) {
    show(this, message, Toast.LENGTH_SHORT)
}

fun Context.longToast(message: Int) {
    show(this, message, Toast.LENGTH_LONG)
}

fun Context.longToast(message: CharSequence?) {
    show(this, message, Toast.LENGTH_LONG)
}

fun Fragment.toast(message: Int) = requireActivity().toast(message)

fun Fragment.toast(message: CharSequence) = requireActivity().toast(message)

fun Fragment.longToast(message: Int) = requireContext().longToast(message)

fun Fragment.longToast(message: CharSequence) = requireContext().longToast(message)
