package com.sunrun.sunrunframwork.view.title;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunrun.sunrunframwork.R;
import com.sunrun.sunrunframwork.utils.log.Logger;


/**
 * @作者: Wang'sr
 * @时间: 2016/11/2
 * @功能描述:顶部菜单的公共类
 */

public class BaseTitleLayoutView extends RelativeLayout {
    private View rootView;
    private Context mContext;

    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivLeftIcon;
    private ImageView ivRightIcon;
    private TextView tvTitle;
    CustomLayoutHelper customLayoutHelper;

    public LinearLayout getRelLeftArea() {
        return relLeftArea;
    }

    public void setRelLeftArea(LinearLayout relLeftArea) {
        this.relLeftArea = relLeftArea;
    }

    public RelativeLayout getRelRightArea() {
        return relRightArea;
    }

    public void setRelRightArea(RelativeLayout relRightArea) {
        this.relRightArea = relRightArea;
    }

    private LinearLayout relLeftArea;
    private RelativeLayout relRightArea;
    private Drawable leftIconDrawable;
    private Drawable rightIconDrawable;
    private String leftText;
    private String rightText;
    private String titleText;
    private Boolean isShowRightImg, isShowLeftImg;
    int titleColor;

    public BaseTitleLayoutView(Context context) {
        this(context, null);
    }

    public BaseTitleLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BaseTitleLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**获取资源属性*/
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseTitleLayoutView);
        customLayoutHelper = new CustomLayoutHelper(this);
        loadView(context);
        customLayoutHelper.loadFromAttributes(attrs, defStyleAttr);
        leftIconDrawable = ta.getDrawable(R.styleable.BaseTitleLayoutView_TitlesLeftIcon);
        rightIconDrawable = ta.getDrawable(R.styleable.BaseTitleLayoutView_TitlesRightIcon);
        leftText = ta.getString(R.styleable.BaseTitleLayoutView_TitlesLeftTVText);
        rightText = ta.getString(R.styleable.BaseTitleLayoutView_TitlesRightTVText);
        titleText = ta.getString(R.styleable.BaseTitleLayoutView_TitlesText);
        titleColor = ta.getColor(R.styleable.BaseTitleLayoutView_TitlesColor, 0);
        isShowRightImg = ta.getBoolean(R.styleable.BaseTitleLayoutView_TitlesRightIconIsShow, false);
        isShowLeftImg = ta.getBoolean(R.styleable.BaseTitleLayoutView_TitlesLeftIconIsShow, true);
        ta.recycle();
        initView(context);
    }

    void loadView(Context context) {
        this.mContext = context;
//        if(isInEditMode())return;
        if (rootView != null) return;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mContext instanceof Activity) {
            inflater = ((Activity) mContext).getLayoutInflater();
        }
        rootView = inflater.inflate(R.layout.include_title_view, this, false);
        if (rootView != null) {
            tvLeft = (TextView) rootView.findViewById(R.id.tv_left_text);
            tvRight = (TextView) rootView.findViewById(R.id.tv_right_text);
            ivLeftIcon = (ImageView) rootView.findViewById(R.id.iv_left_icon);
            ivRightIcon = (ImageView) rootView.findViewById(R.id.iv_right_icon);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title_text);
            relLeftArea = (LinearLayout) rootView.findViewById(R.id.left_back_area);
            relRightArea = (RelativeLayout) rootView.findViewById(R.id.right_back_area);
            if (titleColor != 0 && !isInEditMode()) {
                tvTitle.setTextColor(titleColor);
            }
            addView(rootView);
        }

    }

    private void initView(Context context) {
        loadView(context);
        setView();

    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {

        if (!isInEditMode() && params instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) params;
            marginLayoutParams.topMargin = getStatusBarHeight();
            Logger.D("状态栏高度: " + getStatusBarHeight() + " " + marginLayoutParams.topMargin);
        }

        super.setLayoutParams(params);
    }


    public int getStatusBarHeight() {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // check theme attrs
            Context mContext = getContext();
            if (mContext instanceof Activity) {
                int[] attrs = {android.R.attr.windowTranslucentStatus,
                        android.R.attr.windowTranslucentNavigation};
                TypedArray a = mContext.obtainStyledAttributes(attrs);
                try {
                    boolean mStatusBarAvailable = a.getBoolean(0, false);
                    if (!mStatusBarAvailable) return 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }


    /**
     * 设置值和参数
     */
    private void setView() {
        setLeftIconDrawable(leftIconDrawable);
        setRightIconDrawable(rightIconDrawable);
        setRightText(rightText);
        setLeftText(leftText);
        setTitleText(titleText);
        relRightArea.setVisibility(isShowRightImg ? View.VISIBLE : View.GONE);
        relLeftArea.setVisibility(isShowLeftImg ? View.VISIBLE : View.GONE);
        if (isShowLeftImg) {
            /**扩展设置公共监听*/
            getRelLeftArea().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("TitleView", "OnClickListener");
                    InputMethodManager imm = (InputMethodManager) mContext
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
                    ((Activity) mContext).onBackPressed();
//                ((Activity) mContext).overridePendingTransition(
//                        R.anim.slide_in_situ, R.anim.push_right_out);
                }
            });
        }

    }

    /**
     * -------------------- Get View 方便扩展 ----------------
     */

    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    public ImageView getIvLeftIcon() {
        return ivLeftIcon;
    }

    public ImageView getIvRightIcon() {
        return ivRightIcon;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    /**
     * -------------------- set View 方便扩展 ----------------
     */

    public void setLeftIconDrawable(Drawable leftIconDrawable) {
        this.leftIconDrawable = leftIconDrawable;
        getIvLeftIcon().setImageDrawable(leftIconDrawable);
    }

    public void setRightIconDrawable(Drawable rightIconDrawable) {
        this.rightIconDrawable = rightIconDrawable;
        getIvRightIcon().setImageDrawable(rightIconDrawable);
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        getTvLeft().setText(leftText);
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        getTvRight().setText(rightText);
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        getTvTitle().setText(titleText);
    }

    public void setIsShowRightImg(Boolean isShowRightImg) {
        this.isShowRightImg = isShowRightImg;
        getIvRightIcon().setVisibility(isShowRightImg ? VISIBLE : GONE);
    }

}
