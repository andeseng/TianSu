package com.sunrun.sunrunframwork.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

public class InputMethodUtil {

	/**
	 * 隐藏软键盘
	 **/
	public static	void off_jp(Activity context) {
		View view = context.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputMgr = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMgr.isActive()) {
				inputMgr.toggleSoftInput(HIDE_NOT_ALWAYS, 0);
			}
		}
	}
	// 隐藏虚拟键盘
	public static void HideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

		}
	}
	public void test(){
//		((InputMethodManager)getActivity().getSystemService(Service.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), HIDE_NOT_ALWAYS);
	}

	// 显示虚拟键盘
	public static void ShowKeyboard(final View v) {
		if(v==null)return ;
		v.clearFocus();
		v.requestFocus();
		AHandler.runTask(new AHandler.Task() {
			public void task() {
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
			}
		},500);

	}

	// 显示虚拟键盘
	public static void HideKeyboard(final View v) {
		if(v==null)return ;
		v.clearFocus();
		AHandler.runTask(new AHandler.Task() {
			public void task() {
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(),0);
//				imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
			}
		},500);

	}

	// 强制显示或者关闭系统键盘

	public static void KeyBoard(final EditText txtSearchKey, final String status) {
		AHandler.runTask(new AHandler.Task() {
			@Override
			public void task() {
				InputMethodManager m = (InputMethodManager) txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if (status.equals("open")) {
					m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
				} else {
					m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
				}
			}
		}, 300);
	}

	// 通过定时器强制隐藏虚拟键盘
	public static void TimerHideKeyboard(final View v) {
		v.clearFocus();
		AHandler.runTask(new AHandler.Task() {
			@Override
			public void task() {
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
				}
			}
		}, 100);

	}

	// 输入法是否显示着
	public static boolean KeyBoard(EditText edittext) {
		boolean bool = false;
		InputMethodManager imm = (InputMethodManager) edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			bool = true;
		}
		return bool;

	}
}
