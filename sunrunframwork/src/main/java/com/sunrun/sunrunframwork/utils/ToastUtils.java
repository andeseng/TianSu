package com.sunrun.sunrunframwork.utils;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.sunrun.sunrunframwork.app.BaseApplication;

/**
 * Toast工具类,封装Toast
 * Created by WQ on 2017/4/20.
 */

public class ToastUtils {
    private static Handler handler=new Handler(Looper.getMainLooper());
    private  static Toast shortToast = Toast.makeText(BaseApplication.getInstance(), "", Toast.LENGTH_SHORT);



    /**
     * 显示短Toast
     *
     * @param msg
     */
    public static void shortToast(Object msg) {
        showToast(msg,Toast.LENGTH_SHORT);
    }

    /**
     * 显示长Toast
     *
     * @param msg
     */
    public static void longToast(Object msg) {
        showToast(msg,Toast.LENGTH_LONG);
    }


    static void showToast(final Object msg,final int duration){
        if(Looper.getMainLooper()==Looper.myLooper()){
            showToastImpl(msg, duration);
        }else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showToastImpl(msg, duration);
                }
            });
        }
    }
    static void showToastImpl(Object msg, int duration) {
        if (msg instanceof Integer) {
            shortToast.setText((Integer) msg);
        } else {
            shortToast.setText(String.valueOf(msg));
        }
        shortToast.setDuration(duration);
        shortToast.show();
    }
}
