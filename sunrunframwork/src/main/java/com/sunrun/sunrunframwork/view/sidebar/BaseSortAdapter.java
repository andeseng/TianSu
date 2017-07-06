package com.sunrun.sunrunframwork.view.sidebar;

import android.app.Activity;
import android.widget.SectionIndexer;

import com.sunrun.sunrunframwork.R;
import com.sunrun.sunrunframwork.adapter.SelectableAdapter;
import com.sunrun.sunrunframwork.adapter.ViewHolder;

import java.util.List;

/**
 * @作者: Wang'sr
 * @时间: 2016/12/20
 * @功能描述:
 */

public class BaseSortAdapter <T extends SortModel> extends SelectableAdapter<T> implements SectionIndexer {
    /**
     * @param mAct  上下文
     * @param datas 数据集
     */
    public BaseSortAdapter(Activity mAct, List datas) {
        super(mAct, datas, R.layout.test_silderitem);
    }

    /**
     * @param mAct      上下文
     * @param datas     数据集
     * @param layoutIds 界面Id
     */
    public BaseSortAdapter(Activity mAct, List datas, int layoutIds) {
        super(mAct, datas, layoutIds);
    }



    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getData().get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getData().get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<T> list) {
       setData(list);
    }

    @Override
    public void fillView(ViewHolder holder, T sortModel, int position) {
//        TextView tvTitle = holder.getView(R.id.title);
//        TextView tvLetter = holder.getView(R.id.catalog);
//        tvTitle.setText(sortModel.getName());
//        //根据position获取分类的首字母的Char ascii值
//        int section = getSectionForPosition(position);
//
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//        if (position == getPositionForSection(section)) {
//            tvLetter.setVisibility(View.VISIBLE);
//            tvLetter.setText(sortModel.getSortLetters());
//        } else {
//            tvLetter.setVisibility(View.GONE);
//        }
    }
}
