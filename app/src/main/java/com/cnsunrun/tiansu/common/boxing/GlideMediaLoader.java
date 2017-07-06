package com.cnsunrun.tiansu.common.boxing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.cnsunrun.tiansu.R;

/**
 * Created by WQ on 2017/5/5.
 */

public class GlideMediaLoader {
    public static void load(Object context,View imgview,String path,int placeholder){
        with(context)
                .load(path).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .placeholder(placeholder).into((ImageView) imgview);
    }
    public static void load(Object context,View imgview,String path){
        load(context, imgview, path, R.drawable.new_nopic);
    }
    public static void loadHead(Object context,View imgview,String path){
        load(context, imgview, path, R.drawable.ic_def_head);
    }
//    public static void loadRound(Object context,View imgview,String path,int radiusDP){
//        RoundedCornersTransformation transformation= new RoundedCornersTransformation(BaoBaoShuApp.getInstance(),radiusDP,0);
//        with(context)
//                .load(path)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder( R.drawable.new_nopic).bitmapTransform(transformation
//        ).dontAnimate().into((ImageView) imgview);
//    }
   static RequestManager with(Object context){
       if(context instanceof Activity){
           return  Glide.with((Activity) context);
       }else if( context instanceof  Fragment ){
           return Glide.with((Fragment) context);
       }else  if (context instanceof Context){
           return Glide.with((Context) context);
       }
       return null;
   }

    public static class ShadowBitMapTransForm implements Transformation<Bitmap>{

        int Radiu=10;
        Context context;
        public ShadowBitMapTransForm(Context context,int radiu) {
            Radiu = radiu;
            this.context=context;
        }

        @Override
        public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
            Bitmap bm=resource.get();
            GradientDrawable mBackShadowDrawableLR;
            //起始颜色和结束颜色
            int[] mBackShadowColors = new int[] { 0x00000000 , 0xB0AAAAAA};
            //线性渐变
            mBackShadowDrawableLR = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
            mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            //这是需要绘制阴影的位置
            mBackShadowDrawableLR.setBounds(0, 0, Radiu , bm.getHeight());
            Canvas canvas = new Canvas(bm);
            //绘制到canvas
            mBackShadowDrawableLR.draw(canvas);

            return BitmapResource.obtain(bm, Glide.get(context).getBitmapPool());
        }

        @Override
        public String getId() {
            return ""+Radiu;
        }
    }

}
