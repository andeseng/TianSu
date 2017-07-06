package com.sunrun.sunrunframwork.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @不能滚动的滚动视图
 */
public class NoneScrollView extends ScrollView
{

	public NoneScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public NoneScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoneScrollView(Context context) {
		super(context);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return false;
	}
}
