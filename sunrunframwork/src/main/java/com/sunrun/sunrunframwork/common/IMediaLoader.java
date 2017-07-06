package com.sunrun.sunrunframwork.common;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Define how media display.
 * 图片加载接口,参考boxing
 *
 * @author ChenSL
 */
public interface IMediaLoader {
    /**
     * display thumbnail images for a ImageView.
     *
     * @param img     the display ImageView. Through ImageView.getTag(R.string.app_name) to get the absolute path of the exact path to display.
     * @param absPath the absolute path to display, may be out of date when fast scrolling.
     * @param width   the resize with for the image.
     * @param height  the resize height for the image.
     */
    void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height);
    void displayImage(@NonNull ImageView img, @NonNull String absPath, IMediaLoadeProgressListener mediaLoadeProgressListener);
    /**
     * display raw images for a ImageView, need more work to do.
     *
     * @param img     the display ImageView.Through ImageView.getTag(R.string.app_name) to get the absolute path of the exact path to display.
     * @param absPath the absolute path to display, may be out of date when fast scrolling.
     */
    void displayRaw(@NonNull ImageView img, @NonNull String absPath);


    interface IMediaLoadeProgressListener {
        void onProgressUpdate(int arg2, int arg3);

        void onLoadingComplete(String url, Drawable  drawable);

        void onLoadingFailed(String errMsg);

    }
}