package com.sunrun.sunrunframwork.uiutils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.sunrun.sunrunframwork.http.BaseConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;


/**
 * BitmapUtils Bitmap工具类
 *
 */
public class BitmapUtils {

    public static int IO_BUFFER_SIZE = 150 * 150;

    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     * <p/>
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     * <p/>
     * B.本地路径:url="file://mnt/sdcard/photo/image.png";
     * <p/>
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param url
     * @return
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {


        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {

            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public static long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    /**
     * @param bitmap
     * @param xScale x 缩放
     * @param yScale y 缩放
     * @return
     */
    public static Bitmap postScale(Bitmap bitmap, float xScale, float yScale) {

        if (xScale == 0 || yScale == 0)
            return bitmap;
        Matrix matrix = new Matrix();
        matrix.postScale(xScale, yScale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * view转Bitmap
     **/
    public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {

        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }


    /**
     * 将控件转换为bitmap
     **/
    public static Bitmap convertViewToBitMap(View view) {
        // 打开图像缓存
        view.setDrawingCacheEnabled(true);
        // 必须调用measure和layout方法才能成功保存可视组件的截图到png图像文件
        // 测量View大小
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 发送位置和尺寸到View及其所有的子View
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        // 获得可视组件的截图
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


    /**
     * 获取屏幕截图的bitmap对象的代码如下
     **/
    public Bitmap getScreenPic(View view) {

        View rootView = view.getRootView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache();
        // 不明白为什么这里返回一个空，有帖子说不能在oncreat方法中调用
        // 测量View大小
        rootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 发送位置和尺寸到View及其所有的子View
        rootView.layout(0, 0, rootView.getMeasuredWidth(), rootView.getMeasuredHeight());
        // 解决措施，调用上面的measure和layout方法之后，返回值就不再为空
        // 如果想要创建的是固定长度和宽度的呢？
        Bitmap bitmap = rootView.getDrawingCache();
        rootView.destroyDrawingCache();
        return bitmap;
    }


    /**
     * Drawable → Bitmap
     **/
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

    }


    /**
     * bitmap → drawable
     **/
    public static Drawable bitmapToDrawable(Context context, String filename) {

        Bitmap image = null;
        BitmapDrawable ddd = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(filename);
            image = BitmapFactory.decodeStream(is);
            ddd = new BitmapDrawable(context.getResources(), image);
            is.close();
        } catch (Exception e) {
        }
        return ddd;

    }


    /**
     * byte[] → Bitmap
     **/
    public static Bitmap byteToDrawable(Context context, byte[] bb) {
        Bitmap pp = BitmapFactory.decodeByteArray(bb, 0, bb.length);
        return pp;
    }


    /**
     * Bitmap → byte[]
     **/
    public static byte[] bitmapToByte(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] yy = baos.toByteArray();
        return yy;
    }


    /**
     * 将text  转换成  bitmap
     **/
    public static Bitmap createTxtImage(String txt, int txtSize) {
        Bitmap mbmpTest = Bitmap.createBitmap(txt.length() * txtSize + 4,
                txtSize + 4, Bitmap.Config.ARGB_8888);
        Canvas canvasTemp = new Canvas(mbmpTest);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        p.setTextSize(txtSize);
        canvasTemp.drawText(txt, 2, txtSize - 2, p);
        return mbmpTest;

    }

    /**
     * 放大缩小图片
     **/
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }


    /**
     * 获得圆角图片的方法
     **/
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    /**
     * 对 bitmap 进行裁剪
     **/
    public Bitmap bitmapClip(Context context, int id, int x, int y) {
        Bitmap map = BitmapFactory.decodeResource(context.getResources(), id);
        map = Bitmap.createBitmap(map, x, y, 120, 120);
        return map;
    }


    /**
     * 图片的倒影效果
     */
    public static Bitmap createReflectedImage(Bitmap originalImage) {
        final int reflectionGap = 4;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        // Create a Bitmap with the flip matrix applied to it.
        // We only want the bottom half of the image
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                height / 2, width, height / 2, matrix, false);

        // Create a new bitmap with same width but taller to fit reflection
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Bitmap.Config.ARGB_8888);

        // Create a new Canvas with the bitmap that's big enough for
        // the image plus gap plus reflection
        Canvas canvas = new Canvas(bitmapWithReflection);
        // Draw in the original image
        canvas.drawBitmap(originalImage, 0, 0, null);
        // Draw in the gap
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        // Draw in the reflection
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        // Create a shader that is a linear gradient that covers the reflection
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        // Set the paint to use this shader (linear gradient)
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }


    /**
     *
     * 压缩图片
     * @param filepath 图片文件路径
     * @param deleteSource
     * @return
     */
    public static   File compressPhoto(String filepath,boolean deleteSource){
        File photoFile=new File(filepath);
        String aimPath = new File(BaseConfig.IMG_SAVE_DIR, photoFile.getName())
                .getAbsolutePath();
        String compress_path = UIUtils.saveBitmapToFile(photoFile.getAbsolutePath(),
                aimPath, 30);// 压缩上传的图片质量
        if(deleteSource) {
            photoFile.delete();//删除原始文件
        }
        return  new File(compress_path);
    }

    /**
     * 压缩图片
     * @param filepath 图片文件路径
     * @return 新文件地址, 源文件不会被删除
     */
    public static   File compressPhoto(String filepath){
       return compressPhoto(filepath,false);
    }
}
