package com.cnsunrun.tiansu.common.utils;

import android.app.Activity;
import android.content.Intent;

import com.cnsunrun.tiansu.MainActivity;
import com.cnsunrun.tiansu.login.activity.FindPasswordStepOneActivity;
import com.cnsunrun.tiansu.login.activity.FindPasswordStepTwoActivity;
import com.cnsunrun.tiansu.login.activity.LoginActivity;
import com.cnsunrun.tiansu.login.activity.RegisteredActivity;
import com.sunrun.sunrunframwork.uibase.BaseActivity;
import com.sunrun.sunrunframwork.utils.BaseStartIntent;

/**
 * 启动其他Activity 统一入口 请给每个方法写上详尽的注释
 */

public class StartIntent extends BaseStartIntent {


    /**
     * 跳转到注册界面
     *
     * @param baseActivity
     */
    public static void startRegisteredActivity(BaseActivity baseActivity) {
        Intent intent = new Intent(baseActivity, RegisteredActivity.class);
        baseActivity.startActivityForResult(intent, 1);
    }

    /**
     * 跳转到登录界面
     *
     * @param baseActivity
     */
    public static void startLoginActivity(BaseActivity baseActivity, boolean isOtherLogin, String type) {
        Intent intent = new Intent(baseActivity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isOtherLogin", isOtherLogin);
        intent.putExtra("login_type",type);
        baseActivity.startActivity(intent);
    }


    /**
     * 跳转到主界面
     *
     * @param baseActivity
     */
    public static void startMainActivity(BaseActivity baseActivity) {
        Intent intent = new Intent(baseActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isLogin", "yes");
        baseActivity.startActivity(intent);
    }


    /**
     * 进入找回密码第一步界面
     *
     * @param activity
     */
    public static void startFindPasswordOneActivity(Activity activity) {
        Intent intent = new Intent(activity, FindPasswordStepOneActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 进入找回密码第二步界面
     *
     * @param activity
     * @param key
     */
    public static void startFindPasswordTwoActivity(Activity activity, String key) {
        Intent intent = new Intent(activity, FindPasswordStepTwoActivity.class);
        intent.putExtra("key", key);
        activity.startActivity(intent);
    }


}
