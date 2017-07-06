package com.sunrun.sunrunframwork.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.view.sidebar.TestSideBarActivity;

/**
 * @作者: Wang'sr
 * @时间: 2016/12/9
 * @功能描述: 启动Intent 工具
 */

public class BaseStartIntent {


    /**
     * 测试侧滑选择的Activity
     *
     * @param activity
     */

    public static void startTestSideBarActivity(Activity activity) {
        Intent intent = new Intent(activity, TestSideBarActivity.class);
        activity.startActivity(intent);
    }


    public static void startActivity(Activity activity, Class otherActivity) {
        Intent intent = new Intent(activity, otherActivity);
        activity.startActivity(intent);
    }
    /**
     * 打开链接
     *
     * @param activity
     * @param url
     */
    public static void OpenUrl(Context activity, String url) {
        if("".equals(url))return ;
        if (!String.valueOf(url).startsWith("http://")&&!String.valueOf(url).startsWith("http")) {
            url = "http://" + url;
        }
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            activity.startActivity(intent);
        } catch (Exception e
                ) {
            e.printStackTrace();
        }
    }
    /************************* 启动系统Intent(包含服务广播) *****************************/
    /**
     * 拨打电话 (方式一调到拨号盘用户选择拨打)
     *
     * @param context
     * @param photoString
     */
    public static void StartCallIntent(Context context, String photoString) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + photoString);
            intent.setData(data);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            UIUtils.shortM("呼叫失败");
        }

    }

    /**
     * 拨打电话 (方式二调 直接拨打)*不推荐
     *
     * @param context
     * @param photoString
     */
    public static void StartCall2Intent(Context context, String photoString) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + photoString);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            UIUtils.shortM("呼叫失败");
            return;
        }
        context.startActivity(intent);
    }


    /**
     * 调用系统短信界面发送短信,
     * @param number
     * @param message
     */
    public static void SendSMS(Context context,String number, String message){
        Uri uri = Uri.parse("smsto:" + number);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.putExtra("sms_body", message);
        context.startActivity(sendIntent);
    }

    
}
