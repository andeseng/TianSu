package com.cnsunrun.tiansu.forum.fragment;

import android.os.Bundle;

import com.cnsunrun.tiansu.R;
import com.cnsunrun.tiansu.common.base.UIBindFragment;
import com.cnsunrun.tiansu.home.fragment.HomeFragment;

/**
 * Created by cnsunrun on 2017-07-06.
 */

public class ForumFragment extends UIBindFragment {


    public static ForumFragment newInstance() {
        ForumFragment forumFragment = new ForumFragment();
        Bundle bundle = new Bundle();
        forumFragment.setArguments(bundle);
        return forumFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forum;
    }
}
