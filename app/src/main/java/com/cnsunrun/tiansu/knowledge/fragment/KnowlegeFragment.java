package com.cnsunrun.tiansu.knowledge.fragment;

import android.os.Bundle;

import com.cnsunrun.tiansu.R;
import com.cnsunrun.tiansu.common.base.UIBindFragment;
import com.cnsunrun.tiansu.home.fragment.HomeFragment;

/**
 * Created by cnsunrun on 2017-07-06.
 */

public class KnowlegeFragment extends UIBindFragment{

    public static KnowlegeFragment newInstance() {
        KnowlegeFragment knowlegeFragment = new KnowlegeFragment();
        Bundle bundle = new Bundle();
        knowlegeFragment.setArguments(bundle);
        return knowlegeFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowlege;
    }
}
