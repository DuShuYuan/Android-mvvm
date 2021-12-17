package com.dsy.mvvm.base

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dsy.mvvm.App
import com.dsy.mvvm.utils.coroutine.Coroutine
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


open class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope by MainScope() {

    val context: Context by lazy { this.getApplication<App>() }
    val disposable = CompositeDisposable()

    val onError = MutableLiveData<Int>()
    val loading = MutableLiveData<Boolean>()

    fun <T> execute(
        scope: CoroutineScope = this,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> T
    ): Coroutine<T> {
        return Coroutine.async(scope, context) { block() }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        cancel()
        disposable.clear()
    }


}