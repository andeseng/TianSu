package com.cnsunrun.tiansu.mine.fragment;

import android.os.Bundle;

import com.cnsunrun.tiansu.R;
import com.cnsunrun.tiansu.common.base.UIBindFragment;

/**
 * Created by cnsunrun on 2017-07-06.
 */

public class MineFragment extends UIBindFragment {


    public static MineFragment newInstance() {
        MineFragment mineFragment = new MineFragment();
        Bundle bundle = new Bundle();
        mineFragment.setArguments(bundle);
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }
}
