package com.cnsunrun.tiansu.home.fragment;

import android.os.Bundle;

import com.cnsunrun.tiansu.R;
import com.cnsunrun.tiansu.common.base.UIBindFragment;

/**
 * 首页
 * Created by WQ on 2017/4/21.
 */

public class HomeFragment extends UIBindFragment  {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

}
