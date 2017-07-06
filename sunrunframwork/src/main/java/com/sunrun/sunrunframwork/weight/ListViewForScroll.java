package com.sunrun.sunrunframwork.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewForScroll extends ListView {

	public ListViewForScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ListViewForScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewForScroll(Context context) {
		super(context);
	}

	boolean isMeasure = true;

	public void isOnMeasure(boolean fag) {
		isMeasure=fag;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = heightMeasureSpec;
		if (isMeasure) {
			expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
		}
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {

//		if (ev.getAction() == MotionEvent.ACTION_MOVE) { return true; // 禁止ListView滑动
//		}
		return super.dispatchTouchEvent(ev);

	}

}
