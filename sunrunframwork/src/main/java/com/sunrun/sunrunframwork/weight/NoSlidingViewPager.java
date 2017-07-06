package com.sunrun.sunrunframwork.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不可滑动的ViewPage
 */

public class NoSlidingViewPager extends ViewPager {


  private boolean noSliding = true;

  public NoSlidingViewPager(Context context) {
    super(context);
  }

  public NoSlidingViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onTouchEvent(MotionEvent arg0) {
    if (noSliding) {
      return false;
    }
    return super.onTouchEvent(arg0);
  }

//  @Override
//  public void setOnPageChangeListener(OnPageChangeListener listener) {
//    super.setOnPageChangeListener(listener);
//  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent arg0) {
    if (noSliding) {
      return false;
    }
    return super.onInterceptTouchEvent(arg0);
  }



  public boolean isNoSliding() {
    return noSliding;
  }

  public void setNoSliding(boolean noSliding) {
    this.noSliding = noSliding;
  }


}
