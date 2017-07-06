package com.sunrun.sunrunframwork.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import com.sunrun.sunrunframwork.R;

import java.util.List;

public class FuncAdapter extends ViewHolderAdapter<FuncAdapter.FuncItem> {

	/**
	 * @author WQ 下午4:14:18
	 * 
	 */
	public static class FuncItem {
		String text;
		boolean isLimit;
		public static int  defColor=Color.parseColor("#666666");
		int color=defColor;
		
		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public FuncItem(String text) {
			super();
			this.text = text;
		}

		public FuncItem(String text, int color) {
			this.text = text;
			this.color = color;
		}

		public FuncItem(String text, boolean isLimit, int color) {
			this.text = text;
			this.isLimit = isLimit;
			this.color = color;
		}

		public FuncItem(String text, boolean isLimit) {
			super();
			this.text = text;
			this.isLimit = isLimit;
		}

	}

	private List<FuncItem> data;

	public FuncAdapter(Context context, List<FuncItem> data) {
		super(context, data, R.layout.dialog_item_home);
		this.data = data;
	}

	@Override
	public void fillView(ViewHolder holder, FuncItem mItem, int position) {
		holder.setText(R.id.text, mItem.text);
		View line = holder.getView(R.id.view_line);
		holder.setTextColor(R.id.text, mItem.getColor());
		if(position==this.getCount()-2)
		{
			LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,px2dip(holder.getmConvertView().getContext(),5));
			line.setLayoutParams(params);
		}
		else {
			LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,px2dip(holder.getmConvertView().getContext(),1));
			line.setLayoutParams(params);
		}
		
		holder.setVisibility(R.id.gap_line, false);
	}
	public static int px2dip(Context context, float pxValue) { 
		final float scale = context.getResources().getDisplayMetrics().density; 
		return (int) (pxValue / scale + 0.5f); 
		} 
		

}
