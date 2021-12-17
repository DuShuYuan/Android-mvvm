package com.dsy.mvvm.base.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ZJP on 2017/3/5.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, List<? extends Fragment> fragments) {
        this(fm,fragments,true);
    }
    public FragmentAdapter(FragmentManager fm, List<? extends Fragment> fragments,boolean isResume) {
        super(fm,isResume?BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT:BEHAVIOR_SET_USER_VISIBLE_HINT);
        this.fragments = fragments;
    }

    public Fragment getItem(int fragment) {
        return fragments.get(fragment);
    }

    public int getCount() {
        return fragments.size();
    }

}
