package com.sunrun.sunrunframwork.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WQ 可选择的适配器
 * @param <T>数据源
 * @选择模式切换后需要手动刷新适配器
 */
public abstract class SelectableAdapter<T> extends ViewHolderAdapter<T> {
    public final static int RADIO = 0x2100; //没有处于编辑的状态  但是是可以编辑的
    public final static int MULTISELECT = 0x2101; //正在编辑的状态
    protected int selectMode = RADIO;
    protected SparseBooleanArray selectList = null;
    protected int selectIndex = -1;
    protected int selectedNum = 0;

    public SelectableAdapter(Context context, int layoutId) {
        super(context, layoutId);
        // TODO Auto-generated constructor stub
        selectMode(RADIO);
    }

    public int getSelectPosition() {
        return selectIndex;
    }

    public int getSelectCount() {
        return selectedNum;
    }

    public T getSelectItem() {
        return getItem(selectIndex);
    }

    public SelectableAdapter(Context context, List<T> data, int res) {
        super(context, data, res);
        // TODO Auto-generated constructor stub
        selectMode(RADIO);
    }

    public SelectableAdapter(Context context, List<T> data,
                             AdapterView<?> adapterView, int res) {
        super(context, data, res);
        // TODO Auto-generated constructor stub
        selectMode(RADIO);
        adapterView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                select(position);
            }
        });
    }

    public void select(int position) {
        setSelectPosition(position);
        notifyDataSetChanged();
    }

    public	void setSelectPosition(int position) {
        if (selectMode == RADIO) {
            selectIndex = position;
            selectedNum = 1;
        } else {
            boolean isSelected = !isSelected(position);
            selectedNum = isSelected ? selectedNum + 1 : selectedNum - 1;
            selectList.put(itemId(position), isSelected);
        }
    }


    //是不是处于没有编辑的状态  如果是处于没有编辑的状态的话 返回的是false  如果处于的是正在编辑的状态的话 那么就要看selectlist集合中
    public boolean isSelected(int position) {
//        if (selectMode == RADIO) {
//            if (selectIndex == position) {
//                return true;
//            }
//         } else {
//            if (selectList.get(itemId(position))) {
//                return true;
//            }
//        }
//        return false;
        return selectMode == RADIO ? selectIndex == position : selectList.get(itemId(position));
    }

    public boolean isSelected(Object item){
        return  selectList
                .get(item.hashCode());
    }

    /**
     *  全选的设置
     * @param isSelect
     */
    public void selectAll(boolean isSelect) {
        // TODO Auto-generated method stub
        if (selectMode == MULTISELECT) {
            for (int i = 0; i < mData.size(); i++)
                selectList.put(itemId(i), isSelect);
            selectedNum = isSelect ? getCount() : 0;
        } else if (!isSelect) {
            selectIndex = -1;
            selectedNum = 0;
        }
        notifyDataSetChanged();
    }

    /**
     *  单选的设置
     * @param position
     * @param mSingleSelected
     */
    public void selectedSingle(int position, boolean mSingleSelected) {
        if (selectMode == MULTISELECT) { //如果正处于编辑的状态
            if (mSingleSelected) { //checkbox 为true 就要添加数据
                for (int i = 0; i < mData.size(); i++){
                    if (i== position ) {
                        selectList.put(itemId(i), mSingleSelected);
                        break;
                    }
                }
            } else { //checkbox 为false  就要把之前添加的那条数据给删除掉
                selectList.delete(itemId(position));
            }

            selectedNum = mSingleSelected ? selectedNum + 1 : selectedNum - 1;
        }
    }

    /**
     * 获取全选的数据集合
     * @return
     */
    public List<T> getAllCheckData(){
        List <T>tmpData=new ArrayList<>();
        if(mData!=null){
            for (int i = 0; i < mData.size(); i++) {
                if(isSelected(i)){
                    tmpData.add(mData.get(i));
                }
            }
        }
        return tmpData;
    }

    /**
     *    获取到单选的集合数据
     * @return
     */
    public List<T> getSingleCheckData(){
        List<T> tmpSingleData = new ArrayList<>();
        if(mData!=null){
            for (int i = 0; i < mData.size(); i++) {
                if(isSelected(i)) {
                    tmpSingleData.add(mData.get(i));
                }
            }
        }
        return tmpSingleData;
    }
    /**
     * 选择模式
     */

    public void selectMode(int mode) {
        if (mode == RADIO || mode == MULTISELECT) {
            this.selectMode = mode;
            selectedNum = 0;
            if (selectMode == MULTISELECT)
                selectList = new SparseBooleanArray(mData.size());
        }
    }

    /**
     * 获得数据源hashCode
     *
     * @param position
     * @return
     */
    private int itemId(int position) {
        if (mData == null || position >= mData.size() || mData.get(position) == null)
            return -1;
        return mData.get(position).hashCode();
    }
}
