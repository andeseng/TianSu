package com.sunrun.sunrunframwork.view.title;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunrun.sunrunframwork.R;

import skin.support.content.res.SkinCompatResources;
import skin.support.utils.SkinLog;
import skin.support.widget.SkinCompatHelper;


/**
 * Created by WQ on 2017/4/20.
 */

public class CustomLayoutHelper extends SkinCompatHelper{
//    private static final String TAG = SkinCompatImageHelper.class.getSimpleName();
    BaseTitleLayoutView titleLayoutView;
    private int leftIconResId=INVALID_ID;
    private int rightIconResId=INVALID_ID;
    private int tvTitleColorId=INVALID_ID;

    //
    private int tvLeftColorId=INVALID_ID;
    private int tvRightColorId=INVALID_ID;
    public CustomLayoutHelper(BaseTitleLayoutView titleLayoutView) {
        this.titleLayoutView = titleLayoutView;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = null;
        try {
            a = TintTypedArray.obtainStyledAttributes(titleLayoutView.getContext(), attrs,
                    R.styleable.BaseTitleLayoutView, defStyleAttr, 0);
            tvTitleColorId = a.getResourceId(R.styleable.BaseTitleLayoutView_TitlesColor, INVALID_ID);
            leftIconResId = a.getResourceId(R.styleable.BaseTitleLayoutView_TitlesLeftIcon, INVALID_ID);
            rightIconResId = a.getResourceId(R.styleable.BaseTitleLayoutView_TitlesRightIcon,INVALID_ID);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
        applySkin();
    }

    public void setLeftImageResource(int resId) {
        leftIconResId = resId;
        applySkin();
    }
    public void setRightImageResource(int resId) {
        rightIconResId = resId;
        applySkin();
    }

    public void applySkin() {
        applyTextColorResource(tvTitleColorId,titleLayoutView.getTvTitle());
        applyImageSrcResource(leftIconResId,titleLayoutView.getIvLeftIcon());
        applyImageSrcResource(rightIconResId,titleLayoutView.getIvRightIcon());
    }
    void applyTextColorResource(int mTextColorResId,TextView textView) {
        mTextColorResId = checkResourceId(mTextColorResId);
        if (mTextColorResId == skin.support.R.color.abc_primary_text_disable_only_material_light
                || mTextColorResId == skin.support.R.color.abc_secondary_text_material_light) {
            return;
        }
        if (mTextColorResId != INVALID_ID) {
            ColorStateList color = SkinCompatResources.getInstance().getColorStateList(mTextColorResId);
            textView.setTextColor(color);
        }
    }
 void   applyImageSrcResource(int mSrcResId,ImageView imageView){
        mSrcResId = checkResourceId(mSrcResId);
        SkinLog.d(TAG, "mSrcResId = " + mSrcResId);
        if (mSrcResId == INVALID_ID) {
            return;
        }
        String typeName = imageView.getResources().getResourceTypeName(mSrcResId);
        if ("color".equals(typeName)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                int color = SkinCompatResources.getInstance().getColor(mSrcResId);
                Drawable drawable = imageView.getDrawable();
                if (drawable instanceof ColorDrawable) {
                    ((ColorDrawable) drawable.mutate()).setColor(color);
                } else {
                    imageView.setImageDrawable(new ColorDrawable(color));
                }
            } else {
                ColorStateList colorStateList = SkinCompatResources.getInstance().getColorStateList(mSrcResId);
                Drawable drawable = imageView.getDrawable();
                DrawableCompat.setTintList(drawable, colorStateList);
                imageView.setImageDrawable(drawable);
            }
        } else if ("drawable".equals(typeName)) {
            Drawable drawable = SkinCompatResources.getInstance().getDrawable(mSrcResId);
            imageView.setImageDrawable(drawable);
        } else if ("mipmap".equals(typeName)) {
            Drawable drawable = SkinCompatResources.getInstance().getMipmap(mSrcResId);
            imageView.setImageDrawable(drawable);
        }
    }

}
