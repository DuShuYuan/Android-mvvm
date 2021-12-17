package com.dsy.mvvm.utils

fun Boolean.toInt():Int{
    return if (this) 1 else 0
}