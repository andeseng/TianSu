package com.sunrun.sunrunframwork.common;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sunrun.sunrunframwork.app.BaseApplication;

/**
 * 默认图片加载器,默认使用glide
 * Created by WQ on 2017/4/18.
 */

public class DefaultMediaLoader implements IMediaLoader {
    IMediaLoader mediaLoader;

    public DefaultMediaLoader() {
        mediaLoader=new IMediaLoader() {
            @Override
            public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
                Glide.with(img.getContext()).
                        load(absPath).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        centerCrop().
                        into(img);
            }

            @Override
            public void displayImage(@NonNull final ImageView img, @NonNull final String absPath, final IMediaLoadeProgressListener mediaLoadeProgressListener) {
                Glide.with(BaseApplication.getInstance()).
                        load(absPath).
                        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                if (mediaLoadeProgressListener != null)
                                    mediaLoadeProgressListener.onLoadingFailed(e.getLocalizedMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (mediaLoadeProgressListener != null)
                                    mediaLoadeProgressListener.onLoadingComplete(absPath,resource);
                                return false;
                            }
                        }).centerCrop().into(img);
            }

            @Override
            public void displayRaw(@NonNull ImageView img, @NonNull String absPath) {
                Glide.with(img.getContext()).load(absPath).crossFade().centerCrop().into(img);
            }
        };
    }

    public void init(IMediaLoader mediaLoader){
        if(mediaLoader==null)throw new NullPointerException("IMediaLoader is null!");
        this.mediaLoader=mediaLoader;
    }

    static DefaultMediaLoader defaultMediaLoader = new DefaultMediaLoader();

    public static IMediaLoader getInstance() {
        return defaultMediaLoader;
    }

    @Override
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
        mediaLoader.displayThumbnail(img, absPath, width, height);
    }

    @Override
    public void displayImage(@NonNull ImageView img, @NonNull String absPath, IMediaLoadeProgressListener mediaLoadeProgressListener) {
        mediaLoader.displayImage(img, absPath, mediaLoadeProgressListener);
    }

    @Override
    public void displayRaw(@NonNull ImageView img, @NonNull String absPath) {
        mediaLoader.displayRaw(img, absPath);
    }
}
