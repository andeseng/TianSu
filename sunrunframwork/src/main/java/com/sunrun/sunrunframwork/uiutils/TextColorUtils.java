package com.sunrun.sunrunframwork.uiutils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.weight.SpanTextView;


/**
 * 文字改变类
 */
public class TextColorUtils {
    /**
     * 关键字高亮
     * @param textView 文本控件
     * @param souceTxt 设置的文字内容
     * @param keywords 关键字
     * @param color    关键字高亮颜色
     */
    public static void keywordsHeightlight(View textView, String souceTxt, String keywords, int color) {
        if(keywords==null|| souceTxt==null)return;
        if(textView instanceof  TextView) {
            SpanTextView.SpanEditable span = new SpanTextView.SpanEditable(souceTxt);
            span.setColorSpanAll(EmptyDeal.dealNull(keywords),color);
            ((TextView)textView).setText(span.commit());
        }
    }

    /**
     * 设置 Text中间颜色
     *
     * @param textView         TextView
     * @param indexStartString 改变文字前面的文字
     * @param changeString     改变的文字
     * @param color            颜色
     */
    public static void setTextColorForIndex(TextView textView, String indexStartString, String changeString, @ColorInt int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(color);
        builder.setSpan(blueSpan, indexStartString.length(), indexStartString.length() + changeString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    /**
     * 设置 Text中间颜色
     *
     * @param textView         TextView
     * @param indexStartString 改变文字前面的文字
     * @param changeString     改变的文字
     * @param color            颜色
     */
    public static void setTextColorForIndex2(Drawable drawable, TextView textView, String indexStartString, String changeString, @ColorInt int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(color);
        ImageSpan imageSpan = new ImageSpan(getVaildDrawable(drawable));
        builder.setSpan(blueSpan, indexStartString.length(), indexStartString.length() + changeString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置图片位置
        textView.setText(builder);
    }


    /**
     * 设置textview 图标,接收drawable对象
     *
     * @param tv
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setCompoundDrawables(TextView tv, Drawable left, Drawable top, Drawable right, Drawable bottom) {

        tv.setCompoundDrawables(getVaildDrawable(left), getVaildDrawable(top), getVaildDrawable(right), getVaildDrawable(bottom));
    }

    /**
     * 设置textview 图标,接收drawable资源id
     *
     * @param tv
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setCompoundDrawables(TextView tv, int left, int top, int right, int bottom) {
        if(tv==null)return ;
        Context context = tv.getContext();
        tv.setCompoundDrawables(getVaildDrawable(context, left), getVaildDrawable(context, top), getVaildDrawable(context, right), getVaildDrawable(context, bottom));
    }

    /**
     * 获取有效的drawable对象,初始化绘制范围
     *
     * @param drawable 原始drawable对象
     * @return
     */
    public static Drawable getVaildDrawable(Drawable drawable) {
        if (drawable == null) return drawable;
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 获取有效的drawable对象,初始化绘制范围
     *
     * @param context
     * @param drawableId drawable 资源id
     * @return
     */
    public static Drawable getVaildDrawable(Context context, int drawableId) {
        if (drawableId == -1 || drawableId == 0) return null;
        return getVaildDrawable(context.getResources().getDrawable(drawableId));
    }

    public static final int Draw_TOP = 0x001;
    public static final int Draw_BUTTOM = 0x002;
    public static final int DRAW_RIGHT = 0x003;
    public static final int Draw_LEFT = 0x004;

    public static void setTextDrawables(Context context, TextView view, int drawabldId, int DrawType) {
        Integer left = 0, top = 0, right = 0, bottom = 0;
        switch (DrawType) {
            case Draw_TOP:
                top = drawabldId;
                break;
            case Draw_BUTTOM:
                bottom = drawabldId;
                break;
            case DRAW_RIGHT:
                right = drawabldId;
                break;
            case Draw_LEFT:
                left = drawabldId;
                break;
        }
        view.setCompoundDrawables(getVaildDrawable(context, left), getVaildDrawable(context, top), getVaildDrawable(context, right), getVaildDrawable(context, bottom));

    }


    /**
     * 设置drawable
     *
     * @param context
     * @param view
     * @param leftResId
     * @param topResId
     * @param rightResId
     * @param bottomResId
     */
    public static void setTextDrawables(Context context, TextView view, Integer leftResId, Integer topResId,
                                        Integer rightResId, Integer bottomResId) {
        if (leftResId != null) {
            Drawable img = context.getResources().getDrawable(leftResId);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            view.setCompoundDrawables(img, null, null, null);
        } else if (topResId != null) {
            Drawable img = context.getResources().getDrawable(topResId);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            view.setCompoundDrawables(null, img, null, null);
        } else if (rightResId != null) {
            Drawable img = context.getResources().getDrawable(rightResId);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            view.setCompoundDrawables(null, null, img, null);
        } else if (bottomResId != null) {
            Drawable img = context.getResources().getDrawable(bottomResId);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            view.setCompoundDrawables(null, null, null, img);
        }

//        view.setCompoundDrawablePadding(5);

    }


}
