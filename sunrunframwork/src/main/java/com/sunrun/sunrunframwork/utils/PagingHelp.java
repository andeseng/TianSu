package com.sunrun.sunrunframwork.utils;

import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.weight.pulltorefresh.PullToRefreshBase;

import java.util.List;

import static com.sunrun.sunrunframwork.utils.EmptyDeal.empty;
import static com.sunrun.sunrunframwork.utils.EmptyDeal.size;


/**
 * 分页帮助类,辅助可分页的页面进行分页操作,封装分页的具体实现 ,借由分页操作接口进行协调.便于分页的统一调整
 * 
 * @author WQ 上午10:46:55
 * @param <T>
 */
public class PagingHelp<T> implements Pagingable<T>, PullToRefreshBase.OnRefreshListener2 {
	protected int curPage = 1;
	protected List<T> mData = null;
	protected BaseAdapter mAdapter;
	protected int pageSize = 10;
	protected PullToRefreshBase pullListView;
	Pagingable<T> pagingable;
	AdapterView list;
	protected boolean addFirst=false;
	
	public boolean isAddFirst() {
		return addFirst;
	}

	public void setAddFirst(boolean addFirst) {
		this.addFirst = addFirst;
	}

//	public static int size=20;
	boolean isNeedTip=true;
	public PagingHelp(Pagingable<T> pagingable) {
		this.pagingable = pagingable;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurPage() {
		return curPage;
	}

	public List<T> getmData() {
		return mData;
	}
	public List<T> setListData(List<T> data){
		return mData=data;
	}
	public boolean canLoad(List<T> data) {
		return !EmptyDeal.isLessThan(data, pageSize-1);
	}

	public void loadData(int curPage) {
		pagingable.loadData(this.curPage = curPage);
	}

	public BaseAdapter getAdapter(List<T> mData) {
		return pagingable.getAdapter(mData);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		reshPage();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		nextPage();
	}

	/**
	 * 设置下拉监听
	 * 
	 * @param pullListView
	 */

	public void setPullListener(PullToRefreshBase pullListView) {
		this.pullListView = pullListView;
		pullListView.setOnRefreshListener(this);
	}

	public void nextPage() {
		isNeedTip=true;
		curPage++;
		loadData(curPage);
	}

	public void reshPage() {
		curPage = 1;
		loadData(curPage);
	}
	

	public synchronized BaseAdapter setDataToView(List<T> mData, AdapterView listView) {
		this.list=listView;
		pageSize=Math.max(size(mData),pageSize);
		if (isFirst()) {
			if (this.mData == null) {
				// 这里是首次加载数据时候做的处理
				listView.setAdapter(mAdapter = getAdapter(this.mData = mData));
			} else {
				// 这里是刷新第1页做的操作
				if (this.mData != mData) {
					this.mData.clear();
					if (mData != null) {
						if(curPage==2) {
							pageSize = Math.max(pageSize, this.mData.size());
						}
						this.mData.addAll(mData);
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		} else {
			if (mData != null) {
				// 这里是在添加,加载更多之后拉下来的数据
				// 将数据 ,除相同项,
				addData2List(this.mData, mData,isAddFirst());
			}
			mAdapter.notifyDataSetChanged();
		}
		if (!canLoad(mData) && pullListView != null) {
			// pullListView.setMode(Mode.PULL_FROM_START);
			if (curPage > 1 &&isNeedTip&&empty(mData))
				UIUtils.shortM("没有更多啦~");
		} else if (pullListView != null) {
			pullListView.setMode(PullToRefreshBase.Mode.BOTH);
		}
		return mAdapter;
	}

	/**
	 * 添加数据到源集合中,替换掉已存在的元素(去重)
	 * 
	 * @param src
	 * @param data
	 */
	public static void addData2List(List src, List data,boolean isAddFirst) {
		if (data != null)
			for (Object object : data) {
				int index = src.indexOf(object);
				if (index != -1) {
					src.set(index, object);
				} else {
					if(isAddFirst && src.size()>0){
						src.add(0,object);
					}else{
						src.add(object);
					}
				}
			}
	}

	@Override
	public void loadCurrentPage() {
		isNeedTip=false;
		int curPage=this.curPage;
		if(list!=null){
			int lastVisiblePostion=list.getLastVisiblePosition();
			curPage=lastVisiblePostion/pageSize+1;
		}
		loadData(curPage);
	}

	public boolean isFirst() {
		return curPage == 1 || this.mData == null;
	}

	@Override
	public void Loadfinish() {
		UIUtils.cancelLoadDialog();
		if (pullListView != null)
			pullListView.onRefreshComplete();
	}

	@Override
	public void loadCurrentPage(int page) {
		isNeedTip=false;
		loadData(page);
	}

}
