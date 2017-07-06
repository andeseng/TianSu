package com.sunrun.sunrunframwork.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * @类名: MMGridView.java
 * @功能描述:自适应大小的GridView
 * @作者 Wangsr
 * @时间 2015-10-12 下午2:00:42
 * @创建版本 V3.0
 */
public class GridViewForScroll extends GridView {


    public GridViewForScroll(Context context) {
        super(context);
    }

    public GridViewForScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewForScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        if(getAdapter()==null ||getAdapter().isEmpty()){
//            return super.onTouchEvent(ev);
//        }
        int motionPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
//        判断触摸区域是否在有效条目中,触摸动作取消时还是交由父级进行处理,防止按下状态显示异常
        if (ev.getAction()!=MotionEvent.ACTION_CANCEL&& motionPosition == INVALID_POSITION) {//触摸在无效区域(无item)时,不做不做处理
            return false;
        }
        return super.onTouchEvent(ev);
    }

}
