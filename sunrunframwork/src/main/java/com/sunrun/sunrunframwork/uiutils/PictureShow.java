package com.sunrun.sunrunframwork.uiutils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sunrun.sunrunframwork.R;
import com.sunrun.sunrunframwork.adapter.FuncAdapter;
import com.sunrun.sunrunframwork.common.DefaultMediaLoader;
import com.sunrun.sunrunframwork.common.IMediaLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片显示
 *
 * @author WQ 下午3:21:30
 */
public class PictureShow {
    public Context mContext;
    List<?> data;
    int position;
    Dialog dialog;
    boolean needNumber = true;
    OnItemClickListener onItemClickListener;
    List<FuncAdapter.FuncItem> funcItems;
    PagerAdapter pagerAdapter;

    public void setNeedNumber(boolean needNumber) {
        this.needNumber = needNumber;
    }

    public String IMG_SAVE_DIR = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            .getAbsoluteFile()
            + "/image";

    public PictureShow(Context mContext) {
        this.mContext = mContext;
    }

    public PictureShow(Context mContext, List<?> imgs,
                       int position) {
        this(mContext);
        setArgment(imgs, position);
    }


    public void setArgment(List<?> imgs, int position) {
        this.data = imgs;
        this.position = position;
    }

    public void setImageOperate(List<FuncAdapter.FuncItem> funcItems, OnItemClickListener onItemClickListener) {
            this.funcItems=funcItems;
        this.onItemClickListener=onItemClickListener;
    }

    public void setArgment(List<?> imgs) {
        this.data = imgs;
    }

    public void setArgment(Object[] imgs) {
        this.data = Arrays.asList(imgs);
    }

    public void setArgment(Object[] imgs, int position) {
        this.data = Arrays.asList(imgs);
        this.position = position;
    }

    public void show() {
        View dialogView = createView(mContext, data, position);
        dialog = UIUtils.createDialog(mContext, dialogView);
    }

    public void refresh() {
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }
    }

    public void dimiss() {
        dialog.cancel();
    }

    /**
     * 创建图片查看视图
     *
     * @param context
     * @param imgs
     * @return
     */
    @SuppressWarnings("deprecation")
    public View createView(final Context context, final List<?> imgs, int position) {
        if (imgs == null || imgs.size() == 0)
            return null;
        View layout = View.inflate(context, R.layout.ui_show_image, null);
        final TextView text_num = (TextView) layout.findViewById(R.id.text_num);
        ViewPager viewPager = (ViewPager) layout.findViewById(R.id.viewpager);

        viewPager.setAdapter(pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public Object instantiateItem(ViewGroup container,
                                          final int position) {
                View imgShow = View.inflate(context, R.layout.item_image, null);
                final ImageView img = (ImageView) imgShow
                        .findViewById(R.id.img_show);
                final View imgload = (View) imgShow.findViewById(R.id.img_load);
                img.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dimiss();
                    }
                });
                img.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        final PictureShow pictureShow = new PictureShow(mContext);
                        final List<FuncAdapter.FuncItem> tmpFuncItems;
                        pictureShow.setArgment(tmpFuncItems = new ArrayList<FuncAdapter.FuncItem>() {
                            {
                                add(new FuncAdapter.FuncItem("保存图片", true));
                                if (funcItems != null) {
                                    addAll(funcItems);
                                }
                                add(new FuncAdapter.FuncItem("取消"));
                            }
                        });

                        pictureShow.showOperaDialog(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int pos, long id) {
                                if (pos == 0) {
                                    UIUtils.showLoadDialog(mContext, "图片保存中...");
                                    DefaultMediaLoader.getInstance().displayImage(img,path(String.valueOf(imgs.get(position))),new IMediaLoader.IMediaLoadeProgressListener(){
                                        @Override
                                        public void onProgressUpdate(int arg2, int arg3) {
                                        }
                                        @Override
                                        public void onLoadingComplete(String url, Drawable drawable) {
                                            saveImage(context,BitmapUtils.drawableToBitmap(drawable));
                                            UIUtils.cancelLoadDialog();
                                            pictureShow.dimiss();
                                        }

                                        @Override
                                        public void onLoadingFailed(String errMsg) {
                                            UIUtils.shortM("图片保存失败");
                                            UIUtils.cancelLoadDialog();
                                            pictureShow.dimiss();
                                        }
                                    });
                                } else if (pos == tmpFuncItems.size() - 1) {
                                    pictureShow.dimiss();
                                } else {
                                    if (onItemClickListener != null) {
                                        onItemClickListener.onItemClick(parent, view, pos, position);
                                    }
                                    pictureShow.dimiss();
                                }
                            }

                        });
                        // UiUtils.shortM("长按,你是要保存图片吗?");
                        return true;
                    }
                });
                // playBackAnim(imgload, R.anim.loadimg_anim);
                if (imgs.get(position) instanceof Integer) {
                    img.setImageResource((Integer) imgs.get(position));
                } else {
                    imgload.setVisibility(View.VISIBLE);
                    DefaultMediaLoader.getInstance().displayImage(img,path(String.valueOf(imgs.get(position))),new IMediaLoader.IMediaLoadeProgressListener(){
                        @Override
                        public void onProgressUpdate(int arg2, int arg3) {
                        }
                        @Override
                        public void onLoadingComplete(String url, Drawable drawable) {
                            imgload.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingFailed(String errMsg) {
                        }
                    });
                }
                Log.d("weiquan", imgs.get(position) + "   ");
                container.addView(imgShow);
                return imgShow;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getItemPosition(Object object) {
//                return super.getItemPosition(object);
                return POSITION_NONE;
            }


            @Override
            public int getCount() {
                return imgs.size();
            }
        });
        viewPager.setCurrentItem(position);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                text_num.setText("" + (arg0 + 1) + "/" + imgs.size());
                text_num.setVisibility(needNumber ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        return layout;
    }

 public    void loadImage(){

    }

    public static void updateGallery(Context context,File file){
        Uri localUri = Uri.fromFile(file);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
        context. sendBroadcast(localIntent);
    }

    void saveImage(Context context, Bitmap
                           loadedImage){
        File savePath = new File(
                IMG_SAVE_DIR,
                System.currentTimeMillis()
                        + ".jpg");
        if (UIUtils
                .saveBitmapToFile(
                        loadedImage,
                        savePath.getAbsolutePath())) {
            updateGallery(context,savePath);
            UIUtils.longM("图片保存在 "
                    + savePath
                    .getAbsolutePath()
                    + " 目录下");
        } else {
            UIUtils.shortM("图片保存失败");
        }
    }
    public static String path(String path) {
        if (!path.startsWith("http")) {
            return "file:///" + path;
        }
        return path;
    }

    public void showOperaDialog(OnItemClickListener itemClick) {
        View dialogView = View.inflate(mContext, R.layout.dialog_home, null);
        dialog = UIUtils.createDialog(mContext, dialogView, true);
        ListView list = (ListView) dialogView.findViewById(R.id.list);
        list.setAdapter(new FuncAdapter(mContext, (List<FuncAdapter.FuncItem>) data));
        list.setOnItemClickListener(itemClick);
    }

    /**
     * @param itemClick
     */
    public void showCenterOperaDialog(OnItemClickListener itemClick) {
        View dialogView = View.inflate(mContext, R.layout.dialog_home, null);
        dialog = UIUtils.createDialog(mContext, dialogView, true);
        ListView list = (ListView) dialogView.findViewById(R.id.list);
        list.setAdapter(new FuncAdapter(mContext, (List<FuncAdapter.FuncItem>) data));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) list.getLayoutParams();
        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//移除原有规则
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.leftMargin = DisplayUtil.dp2px(mContext, 40);
        params.rightMargin = DisplayUtil.dp2px(mContext, 40);
        list.setLayoutParams(params);
        list.setOnItemClickListener(itemClick);
    }
}
