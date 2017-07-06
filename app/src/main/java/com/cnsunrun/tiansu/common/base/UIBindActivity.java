package com.cnsunrun.tiansu.common.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 集成视图绑定和解绑,的Activity 基类
 * Created by WQ on 2017/5/4.
 */

public abstract class UIBindActivity extends TranslucentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        onViewCreated(savedInstanceState);
    }

    public abstract void onViewCreated(Bundle savedInstanceState) ;
    protected abstract int getLayoutId();
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        bindView();
    }


    @Override
    protected boolean isTranslucent() {
        return true;
    }

    private void bindView() {
        ButterKnife.bind(this);
    }
    private void unbindView() {
        ButterKnife.unbind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        bindView();
    }
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        bindView();
    }
    @Override
    protected void onDestroy() {
        unbindView();
        super.onDestroy();
    }
}
