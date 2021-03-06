package com.sunrun.sunrunframwork.uibase;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.sunrun.sunrunframwork.app.BaseApplication;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.NetRequestHandler;
import com.sunrun.sunrunframwork.http.NetServer;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.http.utils.UIUpdateHandler;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.StatisticsUtil;
import com.sunrun.sunrunframwork.utils.SystemBarTintManager;

import java.util.List;


/**
 * @Activity总父类
 * @控件初始化Eg:@ViewInject(R.id.name)TextView name;
 * @ViewInject(value=R.id.name,click="methodName")TextView name;
 * @网络请求方法 Eg:requestAsynGet(new NAction().setRequestCode(1).put("key",
 * "value").
 * setUrl("http://www.cnsunrun.com").setResultDataType(Bean.class));
 * @界面更新方法 public void nofityUpdate(int requestCode, BaseBean bean) { // TODO
 * Auto-generated method stub // 更新界面 }
 */
public abstract class BaseActivity extends AppCompatActivity implements UIUpdateHandler, NetRequestHandler {
    protected NetRequestHandler mNServer; // 网络请求对象
    protected Fragment currentFragment;
    protected BaseActivity that;
    protected SystemBarTintManager mTintManager;
    @Override
    protected void onCreate(Bundle arg0) {
        dealLongTimeBackGround(arg0);
        super.onCreate(arg0);
//        initStatusBar();
        that = this;
        BaseApplication.getInstance().addActivity(this);
        initView();
    }
    public void initStatusBar(){
        if (SystemBarTintManager.isVersonEnough()) {
            setTranslucentStatus(true);
            mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(false);
        }
    }
    /**
     * 设置状态栏背景颜色 低于KITKAT 的版本该方法不生效
     *
     * @param color
     */
    public void setStatusBarTintColor(int color) {
        if (SystemBarTintManager.isVersonEnough())
            mTintManager.setStatusBarTintColor(color);
    }

    /**
     * 设置状态栏背景Drawable对象 低于KITKAT 的版本该方法不生效
     *
     * @param drawable
     */
    public void setStatusBarTintDrawable(Drawable drawable) {
        if (SystemBarTintManager.isVersonEnough())
            mTintManager.setStatusBarTintDrawable(drawable);
    }

    @TargetApi(19)
    public SystemBarTintManager getSysemBarTintManager() {
        return mTintManager;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    protected void onStart() {
        super.onStart();
        StatisticsUtil.pageStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        StatisticsUtil.pageEnd(this);
    }

    /**
     * 初始化视图(子类中视图初始化工作可在此完成)
     */
    protected  void initView(){};

    protected void onBack() {
        finish();
    }
    /**
     *
     * 处理程序长时间后台的问题, 默认是关闭该Activity,子类若不想被关闭,将该方法置空
     *
     */
    public void dealLongTimeBackGround(Bundle arg0) {
        if (arg0 != null) {
//			if (getClass() != MainActivity.class) {
//				finish();
//			} else {
//				// finish();
//				// start(MainActivity.class);
//			}
        }
    }


    /**
     * 初始化网络请求服务
     */
    final protected void initNetServer() {
        if (mNServer == null)
            mNServer = new NetServer(this, this);
    }

    @Override
    protected void onDestroy() {
        cancelAllRequest();
        BaseApplication.getInstance().removeActivity(this);
        that = null;
        super.onDestroy();
    }

    /**
     * 切换Fragment
     *
     * @param toFragmentClass
     */
    public synchronized void turnToFragment(Class<? extends Fragment> toFragmentClass, int layId) {
        turnToFragment(toFragmentClass, layId, 0, 0);

    }
    /**
     * 切换Fragment
     *
     * @param toFragmentClass
     * @param newAnimId
     * @param oldAnimId
     */
    public synchronized void turnToFragment(Class<? extends Fragment> toFragmentClass, int layId, int newAnimId, int oldAnimId) {
        FragmentManager fm = getSupportFragmentManager();
        // 切换到的Fragment标签
        String toTag = toFragmentClass.getSimpleName();
        // 查找切换的Fragment
        Fragment toFragment = fm.findFragmentByTag(toTag);
        // 如果要切换到的Fragment不存在，则创建
        if (toFragment == null) {
            try {
                toFragment = toFragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (newAnimId != 0 && oldAnimId != 0) {
            ft.setCustomAnimations(newAnimId, oldAnimId);
        }
        if (!toFragment.isAdded()) {
            ft.add(layId, toFragment, toTag);
        } else {
            ft.show(toFragment);
        }
        if (newAnimId != 0 && oldAnimId != 0) {
            ft.commit();
            ft = fm.beginTransaction();
        }
        ft.commitAllowingStateLoss();// 不保留状态提交事务
        List<Fragment> fs = getSupportFragmentManager().getFragments();
        if (fs != null)// 处理因为低内存产生的一些问题
            for (Fragment fragment2 : fs) {
                // 如果当前除了要跳转到的页面外还有其他Fragment正在显示,则隐藏
                if (!fragment2.isHidden() && fragment2 != toFragment)
                    getSupportFragmentManager().beginTransaction().hide(fragment2).commitAllowingStateLoss();
            }

        currentFragment = toFragment;
    }


    /**
     * 重新加载数据
     */
    protected void reload() {}
    @Override
    public void requestAsynGet(NAction action) {
        initNetServer();
        mNServer.requestAsynGet(action);
    }

    @Override
    public void useCache(boolean useCache) {
        initNetServer();
        mNServer.useCache(useCache);
    }

    @Override
    public void requestAsynPost(NAction action) {
        initNetServer();
        mNServer.requestAsynPost(action);
    }

    @Override
    public void cancelRequest(int requestCode) {
        if (mNServer != null)
            mNServer.cancelRequest(requestCode);
    }

    @Override
    public void cancelAllRequest() {
        if (mNServer != null)
            mNServer.cancelAllRequest();
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
    }

    @Override
    public void nofityUpdate(int requestCode, float progress) {
    }

    @Override
    public void dealData(int requestCode, BaseBean bean) {
    }

    @Override
    public void loadFaild(int requestCode, BaseBean bean) {
        UIUtils.shortM(bean);
    }

    @Override
    public void emptyData(int requestCode, BaseBean bean) {

    }

    @Override
    public void requestFinish() {
        UIUtils.cancelLoadDialog();
    }

    @Override
    public void requestCancel() {
        UIUtils.cancelLoadDialog();
    }

    @Override
    public void requestStart() {

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    @Override
    public NetSession getSession() {
        return NetSession.instance(this);
    }


    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        this.startActivity(intent);

    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        this.startActivity(intent);

    }




}
