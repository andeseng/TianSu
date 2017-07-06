package com.sunrun.sunrunframwork.view.sidebar;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @作者: Wang'sr
 * @时间: 2016/12/20
 * @功能描述:  排序工具方法
 */

public class SideBarUtils  {
    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    public static List<SortModel> filledData(String [] date){
        //实例化汉字转拼音类
        CharacterParser characterParser = CharacterParser.getInstance();
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }
    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    public static void filledData(List<?extends SortModel > date){
        //实例化汉字转拼音类
        CharacterParser characterParser = CharacterParser.getInstance();
//        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0,length=date.size(); i<length; i++){
            SortModel sortModel = date.get(i);
//            sortModel.setName(String.valueOf(date.get(i)));
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(String.valueOf(date.get(i)));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

//            mSortList.add(sortModel);
        }
//        return mSortList;

    }
    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     * @param SourceDateList
     */
    private void filterData(String filterStr, List<SortModel> SourceDateList){
        List<SortModel> filterDateList = new ArrayList<SortModel>();
        //实例化汉字转拼音类
        CharacterParser characterParser = CharacterParser.getInstance();
        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : SourceDateList){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

//        // 根据a-z进行排序
//        Collections.sort(filterDateList, pinyinComparator);
//        adapter.updateListView(filterDateList);
//        if(filterDateList.size()<=0){
//            mClearEditText.startAnimation(ClearEditText.shakeAnimation(5));
//        }
    }

}
