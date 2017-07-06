package com.sunrun.sunrunframwork.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕尺寸工具
 * Created by cnsunrun on 2016/11/28.
 */

public class ScreenUtils {
    public static int WHD[];

    /**
     * 获取设备宽高密度等信息信息
     *
     * @param act
     */
    public static int[] initWHD(Activity act) {
        if (WHD == null)
            WHD = WHD(act);
        return WHD;
    }
    /**
     * 获取宽高密度信息
     *
     * @param context
     * @return [0]宽 [1]高 [2]密度
     */
    public static int[] WHD(@NonNull Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager mm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]
                { outMetrics.widthPixels, outMetrics.heightPixels,
                        (int) outMetrics.density };
    }
}
