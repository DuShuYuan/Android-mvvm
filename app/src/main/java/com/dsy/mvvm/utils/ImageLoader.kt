@file:SuppressLint("CheckResult")

package com.dsy.mvvm.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.load(url: String, options: (RequestOptions.() -> Unit)? = null) {
    val requestOptions = RequestOptions()
    options?.invoke(requestOptions)
    Glide.with(context)
            .load(url)
            .apply(requestOptions)
            .into(this)
}

fun ImageView.loadPhoto(url: String, forceLoad: Boolean = false) {
    Glide.with(context)
            .load(url)
            .circleCrop()
            .into(this)
}
