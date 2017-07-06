package com.sunrun.sunrunframwork.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

public class MyExpandableListView extends ExpandableListView {

	public MyExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyExpandableListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true; // 禁止ListView滑动
		}
		return super.dispatchTouchEvent(ev);

	}

}
