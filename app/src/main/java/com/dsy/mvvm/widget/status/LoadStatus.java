package com.dsy.mvvm.widget.status;

import com.dsy.mvvm.R;

public enum LoadStatus {
    SUCCESS,
    LOADING,
    ERROR(R.string.loading_fail, R.mipmap.ic_launcher,R.string.retry),
    EMPTY(R.string.empty,R.mipmap.ic_launcher,0),
    ;

    public int emptyTxt;
    public int emptyImg;
    public int emptyBtn;
    public int loadingTxt;

    LoadStatus(int emptyTxt, int emptyImg, int emptyBtn) {
        this.emptyTxt = emptyTxt;
        this.emptyImg = emptyImg;
        this.emptyBtn = emptyBtn;
    }

    LoadStatus() {
    }

    LoadStatus(int loadingTxt) {
        this.loadingTxt = loadingTxt;
    }
}