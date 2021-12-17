package com.dsy.mvvm.utils

import com.scwang.smart.refresh.layout.SmartRefreshLayout

fun SmartRefreshLayout.loadSuccess(hasNext: Boolean = false) {
    if (isRefreshing) {
        if (hasNext) {
            finishRefresh()
        } else {
            finishRefreshWithNoMoreData()
            finishLoadMoreWithNoMoreData()
        }
    } else if (isLoading){
        if (hasNext) {
            finishLoadMore()
        } else {
            finishLoadMoreWithNoMoreData()
        }
    }
}
fun SmartRefreshLayout.loadFailed(){
    if (isRefreshing) {
        finishRefresh(false)
    } else if (isLoading){
        finishLoadMore(false)
    }
}