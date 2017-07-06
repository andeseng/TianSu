package com.sunrun.sunrunframwork.weight.pulltorefresh.fixtool;


import com.sunrun.sunrunframwork.weight.pulltorefresh.PullToRefreshBase;

public class ScrollFix {
	public static void fixScrollBug(final PullToRefreshBase pullView, int l, int t, int oldl, int oldt){
		if (t <= 5) {//下拉出顶部时重置顶部加载样式,为解决魅族下拉悬停与下拉刷新的冲突
			pullView.postDelayed(new Runnable() {
				@Override
				public void run() {
					pullView.onReset();
				}
			}, 100);
		}
	}
}
