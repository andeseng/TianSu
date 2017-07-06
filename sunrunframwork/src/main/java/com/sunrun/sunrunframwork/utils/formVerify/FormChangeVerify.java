package com.sunrun.sunrunframwork.utils.formVerify;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 表单页面改动验证
 * Created by WQ on 2017/2/16.
 */

public class FormChangeVerify {
    int changeBeforeHashCode = 0;

    /**
     * 改变之前的布局状态记录
     * @param rootView
     */
    public void changeBefore(final View rootView) {
        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeBeforeHashCode
                        = calculateViewHashCode(rootView);
            }
        },100);

    }

    /**
     * 计算布局的hash值
     * @param rootView
     * @return
     */
    private int calculateViewHashCode(View rootView) {
        int viewHashCode = 0;
        if (rootView instanceof AdapterView) {
            AdapterView adapterView= (AdapterView) rootView;
            Adapter adapter=adapterView.getAdapter();
            if(adapter==null){
                return  0;
            }else {
                for (int i = 0,len=adapter.getCount(); i < len; i++) {
                    View itemView=adapter.getView(i,null,adapterView);
                    viewHashCode=viewHashCode+calculateViewHashCode(itemView);
                }
            }
        } else if (rootView instanceof ViewGroup) {
            ViewGroup parentView = (ViewGroup) rootView;
            for (int i = 0, len = parentView.getChildCount(); i < len; i++) {
                View childView = parentView.getChildAt(i);
                viewHashCode = viewHashCode + calculateViewHashCode(childView);
            }
        } else {
            if (rootView instanceof CompoundButton) {
                viewHashCode = String.valueOf(((CompoundButton) rootView).isChecked()).hashCode();
            } else if (rootView instanceof TextView) {
                viewHashCode = (((TextView) rootView).getText().toString()).hashCode();
            } else {
                viewHashCode = 1;
            }
        }
        return viewHashCode;
    }

    /**
     * 验证布局内容是否有改动
     * @param rootView
     * @throws Exception
     */
    public void verify(View rootView) throws FormatException {
        int currentHashCode=calculateViewHashCode(rootView);
        if(changeBeforeHashCode !=currentHashCode){
            throw new FormatException("布局已改变");
        }
    }
}
