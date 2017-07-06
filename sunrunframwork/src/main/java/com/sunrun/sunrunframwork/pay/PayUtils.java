package com.sunrun.sunrunframwork.pay;

import android.app.Activity;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;

import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NetServer;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.http.utils.UIUpdateHandler;
import com.sunrun.sunrunframwork.pay.alipay.AlipayUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.wxapi.WXPayEntryActivity;


/**
 *	支付工具类
 * @author WQ 2015年10月30日
 */
public class PayUtils implements UIUpdateHandler {
	NetServer netServer;
	Activity mContext;
	public final static int ALIPAY = 0x0912;
	public final static int WXPAY = 0x0913;

	public static class PayInfo {

		public String name;// 商品名称
		public String description;// 商品描述或者后台所需的商品信息
		public String price;// 价格
		public String orderNo;// 订单号
		public String notifyUrl;// 异步通知地址
		public int payMethod = ALIPAY;

		protected void deal() {
			if (description == null)
				description = name;
			if (payMethod == WXPAY) {
				price = "" + (int) (Float.parseFloat(price) * 100);
			}

		}
		public PayInfo(String name,String description,String price,int payMethod)
		{
			this.name = name;
			this.description = description;
			this.price = price;
			this.payMethod = payMethod;
		}
	}

	public static abstract class onPayListener {
		public final static int SUCCESS = 0x01;
		public final static int FAILD = 0x02;
		public final static int CANCEL = 0x03;

		public void startPay() {}

		public boolean payFinish(int status) {
			return true;
		}
	}

	onPayListener listener;

	public void pay(PayInfo info, onPayListener listene) {
		info.deal();
		listener = listene;
		if (listener == null) {
			listener = new onPayListener() {};
		}
		
		if (info.payMethod == ALIPAY) {
			listener.startPay();
			new AlipayUtils(mContext).requestPay(info.orderNo, info.name, info.description,
					info.price, info.notifyUrl, new Callback() {

						@Override
						public boolean handleMessage(Message msg) {
							if (TextUtils.equals("" + msg.obj, "9000")) {
								if (listener.payFinish(onPayListener.SUCCESS)) {
									
								}

							} else {
								if (listener.payFinish(onPayListener.FAILD)) {
									//UiUtils.shortM("支付失败");
								}
							}
							return false;
						}
					});;
		} else if (info.payMethod == WXPAY) {
			new WXPayUtils(mContext).createPrepayOrder(mContext, info.name, info.description,
					info.orderNo, Integer.parseInt(info.price), info.notifyUrl,
					new WXPayEntryActivity.PayProgressListener() {
						@Override
						public void onStartPay() {
							UIUtils.shortM("正在启动微信客户端..");
							listener.startPay();
						}

						@Override
						public void onPayFinish(int payresult) {
							if (payresult == 0) {
								if (listener.payFinish(onPayListener.SUCCESS)) {
									UIUtils.shortM("支付成功");
								}

							} else {
								if (listener.payFinish(onPayListener.FAILD)) {
									//UiUtils.shortM("支付失败");
								}
							}
						}
					});
		}
	}

	public PayUtils(Activity context) {
		netServer = new NetServer(this.mContext = context, this);
	}

	@Override
	public void nofityUpdate(int requestCode, BaseBean bean) {
		// TODO Auto-generated method stub

	}



	@Override
	public void nofityUpdate(int requestCode, float progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dealData(int requestCode, BaseBean bean) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFaild(int requestCode, BaseBean bean) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emptyData(int requestCode, BaseBean bean) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestFinish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public NetSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}
}
