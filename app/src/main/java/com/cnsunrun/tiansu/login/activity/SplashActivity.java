package com.cnsunrun.tiansu.login.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.cnsunrun.tiansu.R;
import com.cnsunrun.tiansu.common.base.UIBindActivity;
import com.cnsunrun.tiansu.common.quest.Config;
import com.sunrun.sunrunframwork.adapter.ImagePagerAdapter;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.common.DefaultMediaLoader;
import com.sunrun.sunrunframwork.utils.AHandler;
import com.sunrun.sunrunframwork.weight.AutoScrollViewPager;

import java.io.IOException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 启动页(引导页)
 */
public class SplashActivity extends UIBindActivity {

    //用户进入app的次数
    int startNum = 0;
    //测试  想搞成20秒
    long time = 20000;
    static SplashActivity act;
    AHandler.Task task;
    @Bind(R.id.view_pager)
    AutoScrollViewPager viewPager;
    @Bind(R.id.start)
    View start;
    @Bind(R.id.iv_splash)
    ImageView ivSplash;
    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        act = this;
        startNum = Integer.parseInt(Config.getConfigInfo(this, Config.START_NUM, "0"));// 启动次数
        Config.putConfigInfo(this, Config.START_NUM, String.valueOf(startNum + 1));// 启动次数加1
        if (startNum <= 0) {
            //如果是用户是第一次进入app的话
            try {
                viewPager.setVisibility(View.VISIBLE);
                viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
                String[] bgs = getAssets().list(Config.START_IMG);
                for (int i = 0; i < bgs.length; i++) {
                    bgs[i] = Config.START_IMG + "/" + bgs[i];
                }
                viewPager.setAdapter(new ImagePagerAdapter<String>(this, Arrays.asList(bgs)) {
                            @Override
                            protected void loadImage(@NonNull ImageView img, @NonNull String absPath) {
                                DefaultMediaLoader.getInstance().displayImage(img, absPath, null);
                            }
                        }.setInfiniteLoop(false)
                                .setOnBannerClickListener(new ImagePagerAdapter.OnBannerClickListener() {
                                    @Override
                                    public void onBannerClick(int position, Object t) {
                                        if (viewPager.getAdapter() != null && viewPager.getCurrentItem() != viewPager.getAdapter().getCount() - 1) {
                                            return;
                                        }
                                        startLogin(null);
                                    }
                                })
                )

                ;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            AHandler.runTask(task = new AHandler.Task() {
                @Override
                public void update() {
                    startLogin(null);
                }
            }, time);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }


    public static SplashActivity getSplash() {
        return act;
    }

    public static void close() {
        if (act != null) {
            if (act.task == null) {
                act.finish();
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        AHandler.cancel(task);
        act = null;
    }

    @Override
    public void loadFaild(int requestCode, BaseBean bean) {
        super.loadFaild(requestCode, bean);
    }

    public void startLogin(View view) {
//        if (LoginActivity.isLogin(this, true)) {
//            StartIntent.startMainActivity(this);
//            if(TextUtils.isEmpty(ACache.get(that).getAsString("isBind"))){
//                StartIntent.startBindPhoneActivity(that);
//            }else{
//                StartIntent.startMainActivity(this);
//            }

//        }
        finish();
    }

    @OnClick(R.id.start)
    public void onClick() {
        if (viewPager.getAdapter() != null && viewPager.getCurrentItem() != viewPager.getAdapter().getCount() - 1) {
            return;
        }
        startLogin(null);
    }
}
