package com.cnsunrun.tiansu.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.cnsunrun.tiansu.R;
import com.cnsunrun.tiansu.common.adapter.ViewPagerFragmentAdapter;
import com.cnsunrun.tiansu.common.base.UIBindActivity;
import com.cnsunrun.tiansu.common.utils.DensityUtils;
import com.cnsunrun.tiansu.forum.fragment.ForumFragment;
import com.cnsunrun.tiansu.home.fragment.HomeFragment;
import com.cnsunrun.tiansu.knowledge.fragment.KnowlegeFragment;
import com.cnsunrun.tiansu.main.mode.BottomTab;
import com.cnsunrun.tiansu.mine.fragment.MineFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.sunrun.sunrunframwork.uiutils.UIUtils.shortM;

public class MainActivity extends UIBindActivity {

    @Bind(R.id.activity_main)
    RelativeLayout activityMain;
    private List<Fragment> baseFragments;
    ViewPagerFragmentAdapter mVPAdapter;
    private String[] mTitles = {"首页", "知识", "论坛", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,  R.mipmap.ic_launcher};
    private int[] mIconSelectIds = {
            R.mipmap.ic_launcher,  R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,  R.mipmap.ic_launcher};
    @Bind(R.id.vp_main_center)
    ViewPager mViewPager;
    @Bind(R.id.tab_layout)
    CommonTabLayout mTabLayout;
    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        //初始化
        baseFragments = new ArrayList<>();
        baseFragments.add(HomeFragment.newInstance());
        baseFragments.add(KnowlegeFragment.newInstance());
        baseFragments.add(ForumFragment.newInstance());
        baseFragments.add(MineFragment.newInstance());
        mVPAdapter = new ViewPagerFragmentAdapter(this.getSupportFragmentManager());
        mVPAdapter.setFragments(baseFragments);
        mViewPager.setAdapter(mVPAdapter);
        mViewPager.setOffscreenPageLimit(baseFragments.size());
        setTabData(mTabLayout, mTitles, mIconSelectIds, mIconUnselectIds);
        bindTabEvent(mTabLayout, mViewPager);
        //设置右上角的小红点
//        mTabLayout.showDot(0);
//        mTabLayout.showDot(1);
        setMsgViewMargin(mTabLayout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 设置标签视图内容,
     *
     * @param tabLayout
     * @param titles        文字
     * @param selDrawable   图标/选中
     * @param unselDrawable 图标/未选中
     */
    public void setTabData(CommonTabLayout tabLayout, String titles[], int selDrawable[], int unselDrawable[]) {
        ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            customTabEntities.add(BottomTab.createTab(titles[i], selDrawable[i], unselDrawable[i]));
        }
        tabLayout.setTabData(customTabEntities);
        setMsgViewMargin(tabLayout);
    }

    void setMsgViewMargin(CommonTabLayout tabLayout) {

        for (int i = 0, len = tabLayout.getTabCount(); i < len; i++) {
            tabLayout.setMsgMargin(i, -3, 0);
            MsgView msgView = tabLayout.getMsgView(i);
            msgView.setBackgroundColor(Color.parseColor("#FF3F4E"));
            UIUtils.setViewWH(msgView, DensityUtils.dip2px(this, 7), DensityUtils.dip2px(this, 7));
        }
    }

    /**
     * 绑定标签视图和viewPager 事件
     *
     * @param tabLayout
     * @param viewPager
     */
    void bindTabEvent(final CommonTabLayout tabLayout, final ViewPager viewPager) {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Utils.isQuck(1000)) {
            //结束使用App
//            String id = ACache.get(that).getAsString("use_app_id");
//            if (!TextUtils.isEmpty(id))
//                BaseQuestStart.endUseApp(that, Integer.valueOf(id));
            super.onBackPressed();
        } else {
            shortM("再按一次退出程序");
        }
    }


    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
        }
        super.nofityUpdate(requestCode, bean);
    }


    /**
     *   MainActivity切换fragment的方法
     * @param index
     */
    public void switchPage(int index) {
        mViewPager.setCurrentItem(index);
    }

}
