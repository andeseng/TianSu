package com.sunrun.sunrunframwork.weight.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.sunrun.sunrunframwork.R;
import com.sunrun.sunrunframwork.weight.pulltorefresh.extras.GradationScrollView;
import com.sunrun.sunrunframwork.weight.pulltorefresh.fixtool.ScrollFix;

/**
 * 可监听ScrollView 滚动状态的下拉刷新控件
 * Created by WQ on 2017/2/21.
 */

public class PullToRefreshGradationScrollView extends PullToRefreshBase<GradationScrollView> {

    public PullToRefreshGradationScrollView(Context context) {
        super(context);
    }

    public PullToRefreshGradationScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshGradationScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshGradationScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected GradationScrollView createRefreshableView(Context context, AttributeSet attrs) {
        GradationScrollView scrollView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            scrollView = new PullToRefreshGradationScrollView.InternalScrollViewSDK9(context, attrs);
        } else {
            scrollView = new GradationScrollView(context, attrs);
        }

        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

    @TargetApi(9)
    final class InternalScrollViewSDK9 extends GradationScrollView {
        int _scrollY;
        public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
//			addOnLayoutChangeListener(new OnLayoutChangeListener() {
//          轮播图会导致布局自动往下滚动,所以注释了
//				@Override
//				public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//					smoothScrollBy(getScrollX(), _scrollY);
//				}
//			});
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshGradationScrollView.this, deltaX, scrollX, deltaY, scrollY,
                    getScrollRange(), isTouchEvent);

            return returnValue;
        }
        @Override
        protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
            return 0;
        }
        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            _scrollY=getScrollY();
            ScrollFix.fixScrollBug(PullToRefreshGradationScrollView.this, l, t, oldl, oldt);
            super.onScrollChanged(l, t, oldl, oldt);
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }
}