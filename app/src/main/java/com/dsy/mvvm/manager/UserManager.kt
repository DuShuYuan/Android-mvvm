package com.dsy.mvvm.manager

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.dsy.mvvm.bean.UserBean
import com.dsy.mvvm.utils.coroutine.Coroutine
import com.dsy.mvvm.utils.GSON
import com.dsy.mvvm.utils.fromJsonObject
import splitties.init.appCtx

val UserModel = MutableLiveData(UserManager.user)

fun checkLogin(success:()->Unit){
    checkLogin(success,{})
}
fun checkLogin(success:()->Unit,fail:()->Unit){
    if (UserManager.isLogin){
        success()
    }else{
        fail()
    }
}
object UserManager {
    private const val USER_CACHE = "user_cache"
    private const val USER_INFO = "user_info"

    private val spUser = appCtx.getSharedPreferences(USER_CACHE, Context.MODE_PRIVATE)
    private var userBean: UserBean? = null

    @JvmStatic
    var user: UserBean
        get() {
            if (userBean == null) {
                userBean = GSON.fromJsonObject(spUser.getString(USER_INFO, "")) ?: UserBean()
            }
            return userBean!!
        }
        set(value) {
            val oldAccount = user.account
            userBean = value
            if (oldAccount != value.account) {
                //更换登录用户
                loginChange()
            }
            //保存用户信息
            spUser.edit {
                putString(USER_INFO, GSON.toJson(value))
            }
            UserModel.postValue(value)
        }

    /**
     * 编辑修改用户信息
     */
    fun edit(action: UserBean.() -> Unit) {
        action(user)
        spUser.edit {
            putString(USER_INFO, GSON.toJson(user))
        }
        UserModel.postValue(user)
    }

    val isLogin: Boolean
        get() {
            return user.account.isNotEmpty()
        }

    @JvmStatic
    fun loginChange() {
        Coroutine.async {
            /**
             * 重置用户信息
             */
            UserConfig.clear()//用户app设置信息
        }
    }
}