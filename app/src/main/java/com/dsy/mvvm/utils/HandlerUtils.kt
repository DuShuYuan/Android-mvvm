package com.dsy.mvvm.utils

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

fun runOnUI(function: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        function()
    } else {
        Handler(Looper.getMainLooper()).post(function)
    }
}

fun CoroutineScope.runOnIO(function: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        launch(IO) {
            function()
        }
    } else {
        function()
    }
}