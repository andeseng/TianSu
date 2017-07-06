package com.suo.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.sunrun.sunrunframwork.common.IMediaLoader;

/**
 * 有加载进度显示的ImageView
 * @author WQ 下午2:37:02
 */
public class ProcessScalImageView extends ScaleView implements IMediaLoader.IMediaLoadeProgressListener{

    private Paint mPaint;// 画笔
    int width = 0;
    int height = 0;
    Context context = null;
    int progress = -1;

    public ProcessScalImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ProcessScalImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessScalImageView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#00000000"));// 全透明

        mPaint.setTextSize(sp2px(20));
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setStrokeWidth(2);
        Rect rect = new Rect();
        mPaint.getTextBounds("100%", 0, "100%".length(), rect);// 确定文字的宽度
        if(progress<100&& progress>=0){
        canvas.drawText(progress + "%", getWidth() / 2 - rect.width() / 2,
                getHeight() / 2-rect.height()/2, mPaint);
        }else {
        }

    }
	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public  int sp2px(float spValue) {
		final float fontScale = getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

	@Override
	public void onProgressUpdate( int arg2, int arg3) {
		setProgress((int) (arg2*100.0f/arg3));
	}

    @Override
    public void onLoadingComplete(String url, Drawable drawable) {
        setProgress(100);
    }


    @Override
	public void onLoadingFailed( String arg2) {
		setProgress(-1);
	}


}