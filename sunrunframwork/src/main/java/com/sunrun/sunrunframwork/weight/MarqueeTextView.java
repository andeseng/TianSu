package com.sunrun.sunrunframwork.weight;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {
    boolean isFocused;
    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSingleLine(true);
        setMarqueeRepeatLimit(-1);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
  
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    @Override
    public boolean isFocused() {  
        return isFocused;
    }  
  
}  