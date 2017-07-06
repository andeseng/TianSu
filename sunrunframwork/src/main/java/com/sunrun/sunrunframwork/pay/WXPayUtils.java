package com.sunrun.sunrunframwork.pay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import junit.framework.Assert;
//import net.sourceforge.simcpux.Constants;
//import net.sourceforge.simcpux.MD5;
//import net.sourceforge.simcpux.Util;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;


import com.sunrun.sunrunframwork.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayUtils {
	// appid
		// 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data
		// android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
		public static final String APP_ID = "wx83e77ec01e2699c7";

		// 商户号
		public static final String MCH_ID = "1385559502";

		// API密钥，在商户平台设置
		public static final String API_KEY = "70891e2eb0bac16492f80145972e3b31";
		public static final String WX_NOTIFY_URL = "url"+ "Pay/Pay/wxappnotify";
		
		private Activity act;
		final IWXAPI msgApi;
		private PayReq req;
		private Map<String, String> resultunifiedorder;
		WXPayEntryActivity.PayProgressListener listener;

		public WXPayUtils(Activity act) {
			this.act = act;
			msgApi = WXAPIFactory.createWXAPI(act, null);
			msgApi.registerApp(APP_ID);
			req = new PayReq();
		}

		public String goodsNmae, goodsDetails, productId, out_trade_no, totalFree, url;

		/**
		 * 创建预支付订单
		 * 
		 * @param goodsName
		 * @param goodsDetails
		 * @param productId
		 * @param total_free
		 *            支付金额,单位为分
		 * @param listener
		 */
		/**
		 * @param goodsName
		 * @param goodsDetails
		 * @param out_trade_no
		 * @param total_free
		 *            支付金额,单位为分
		 * @param url
		 *            支付成功回调接口
		 * @param listener
		 */
		public void createPrepayOrder(Context context, String goodsName, String goodsDetails, String out_trade_no, int total_free, String url, WXPayEntryActivity.PayProgressListener listener) {
			this.goodsNmae = goodsName;
			this.goodsDetails = goodsDetails;
			// this.productId = productId;
			this.out_trade_no = out_trade_no;
			this.totalFree = String.valueOf(total_free);
			this.url = url;
			this.listener = listener;
			WXPayEntryActivity.registListener(listener);
			// 生成预支付订单
			GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
			getPrepayId.execute();
		}

		/**
		 * 调用微信进行支付
		 */
		public void genPayReq() {
			if (listener != null)
				listener.onStartPay();
			req.appId = APP_ID;
			req.partnerId = MCH_ID;
			req.prepayId = resultunifiedorder.get("prepay_id");
			req.packageValue = "Sign=WXPay";
			req.nonceStr = genNonceStr();
			req.timeStamp = String.valueOf(genTimeStamp());
			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", req.appId));
			signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
			signParams.add(new BasicNameValuePair("package", req.packageValue));
			signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
			signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
			signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
			req.sign = genAppSign(signParams);
			msgApi.registerApp(APP_ID);
			msgApi.sendReq(req);
		}

		// -------------------------------------异步生成预支付订单---------------------------------------------
		private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

			private ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				dialog = ProgressDialog.show(act, "提示", "正在获取预支付订单...");
				if (listener != null)
					listener.onCreateOrder();
			}

			@Override
			protected void onPostExecute(Map<String, String> result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (listener != null)
					listener.onOrderCreated(WXPayEntryActivity.PayProgressListener.ORDER_CREATE_SUCCESS);
				resultunifiedorder = result;
				genPayReq();
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
				if (listener != null)
					listener.onOrderCreated(WXPayEntryActivity.PayProgressListener.ORDER_CREATE_FAILD);
				WXPayEntryActivity.unRegistListener();
			}

			@Override
			protected Map<String, String> doInBackground(Void... params) {

				String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
				String entity = genProductArgs();

				Log.e("orion1", entity);

				byte[] buf = httpPost(url, entity);

				String content = new String(buf);
				Map<String, String> xml = decodeXml(content);
				return xml;
			}
		}

		// -----------------------------生成订单参数----------------------------------------
		private String genProductArgs() {
			StringBuffer xml = new StringBuffer();

			try {
				String nonceStr = genNonceStr();

				xml.append("</xml>");
				List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
				packageParams.add(new BasicNameValuePair("appid", APP_ID));
				packageParams.add(new BasicNameValuePair("body", goodsNmae));
				packageParams.add(new BasicNameValuePair("detail", goodsDetails));
				// packageParams.add(new BasicNameValuePair("product_id",
				// productId));
				packageParams.add(new BasicNameValuePair("mch_id", MCH_ID));
				packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
				// packageParams.add(new BasicNameValuePair("notify_url",
				// "http://121.40.35.3/test"));
				url=	url==null?WX_NOTIFY_URL:url;
				packageParams.add(new BasicNameValuePair("notify_url", url));
				packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no != null ? out_trade_no : genOutTradNo()));
				packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
				packageParams.add(new BasicNameValuePair("total_fee", totalFree));
				packageParams.add(new BasicNameValuePair("trade_type", "APP"));
				// List<NameValuePair> packageParams = new
				// LinkedList<NameValuePair>();
				// packageParams.add(new BasicNameValuePair("appid",
				// Constants.APP_ID));
				// packageParams.add(new BasicNameValuePair("body", "weixin"));
				// packageParams.add(new BasicNameValuePair("mch_id",
				// Constants.MCH_ID));
				// packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
				// packageParams.add(new BasicNameValuePair("notify_url",
				// "http://121.40.35.3/test"));
				// packageParams.add(new
				// BasicNameValuePair("out_trade_no",genOutTradNo()));
				// packageParams.add(new
				// BasicNameValuePair("spbill_create_ip","127.0.0.1"));
				// packageParams.add(new BasicNameValuePair("total_fee", "1"));
				// packageParams.add(new BasicNameValuePair("trade_type", "APP"));
				String sign = genPackageSign(packageParams);
				packageParams.add(new BasicNameValuePair("sign", sign));

				String xmlstring = toXml(packageParams);

				return xmlstring;

			} catch (Exception e) {
				Log.e(WXPayUtils.class.getSimpleName(), "genProductArgs fail, ex = " + e.getMessage());
				return null;
			}

		}

		/**
		 * 生成订单
		 * 
		 * @return
		 */
		private String genOutTradNo() {
			Random random = new Random();
			return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
		}

		private String genNonceStr() {
			Random random = new Random();
			return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
		}

		private long genTimeStamp() {
			return System.currentTimeMillis() / 1000;
		}

		// -------------------------------签名及请求相关-------------------------------------------------------------------

		/**
		 * 生成签名
		 */

		private String genPackageSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append(API_KEY);

			String packageSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion", packageSign);
			return packageSign;
		}

		private String genAppSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append(API_KEY);

			// this.sb.append("sign str\n"+sb.toString()+"\n\n");
			String appSign = "";
			appSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion", appSign);
			return appSign;
		}

		private String toXml(List<NameValuePair> params) throws UnsupportedEncodingException {
			StringBuilder sb = new StringBuilder();
			sb.append("<xml>");
			for (int i = 0; i < params.size(); i++) {
				sb.append("<" + params.get(i).getName() + ">");

				sb.append(params.get(i).getValue());
				sb.append("</" + params.get(i).getName() + ">");
			}
			sb.append("</xml>");

			Log.e("orion", sb.toString());
			return new String(sb.toString().getBytes(), "ISO8859-1");
		}

		public Map<String, String> decodeXml(String content) {

			try {
				Map<String, String> xml = new HashMap<String, String>();
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(new StringReader(content));
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {

					String nodeName = parser.getName();
					switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if ("xml".equals(nodeName) == false) {
							// 实例化student对象
							xml.put(nodeName, parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					event = parser.next();
				}

				return xml;
			} catch (Exception e) {
				Log.e("orion", e.toString());
			}
			return null;
		}

		public final static String getMessageDigest(byte[] buffer) {
			char hexDigits[] =
			{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			try {
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				mdTemp.update(buffer);
				byte[] md = mdTemp.digest();
				int j = md.length;
				char str[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					str[k++] = hexDigits[byte0 >>> 4 & 0xf];
					str[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(str);
			} catch (Exception e) {
				return null;
			}
		}

		private static final String TAG = "SDK_Sample.Util";

		public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.PNG, 100, output);
			if (needRecycle) {
				bmp.recycle();
			}

			byte[] result = output.toByteArray();
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		public static byte[] httpGet(final String url) {
			if (url == null || url.length() == 0) {
				Log.e(TAG, "httpGet, url is null");
				return null;
			}

			HttpClient httpClient = getNewHttpClient();
			HttpGet httpGet = new HttpGet(url);

			try {
				HttpResponse resp = httpClient.execute(httpGet);
				if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					Log.e(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
					return null;
				}

				return EntityUtils.toByteArray(resp.getEntity());

			} catch (Exception e) {
				Log.e(TAG, "httpGet exception, e = " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}

		public static byte[] httpPost(String url, String entity) {
			if (url == null || url.length() == 0) {
				Log.e(TAG, "httpPost, url is null");
				return null;
			}

			HttpClient httpClient = getNewHttpClient();

			HttpPost httpPost = new HttpPost(url);

			try {
				httpPost.setEntity(new StringEntity(entity));
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				HttpResponse resp = httpClient.execute(httpPost);
				if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					Log.e(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
					return null;
				}

				return EntityUtils.toByteArray(resp.getEntity());
			} catch (Exception e) {
				Log.e(TAG, "httpPost exception, e = " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}

		private static class SSLSocketFactoryEx extends SSLSocketFactory {

			SSLContext sslContext = SSLContext.getInstance("TLS");

			public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
				super(truststore);

				TrustManager tm = new X509TrustManager() {

					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}

					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}
				};

				sslContext.init(null, new TrustManager[]
				{ tm }, null);
			}

			@Override
			public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
				return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
			}

			@Override
			public Socket createSocket() throws IOException {
				return sslContext.getSocketFactory().createSocket();
			}
		}

		private static HttpClient getNewHttpClient() {
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

				return new DefaultHttpClient(ccm, params);
			} catch (Exception e) {
				return new DefaultHttpClient();
			}
		}

		public static byte[] readFromFile(String fileName, int offset, int len) {
			if (fileName == null) { return null; }

			File file = new File(fileName);
			if (!file.exists()) {
				Log.i(TAG, "readFromFile: file not found");
				return null;
			}

			if (len == -1) {
				len = (int) file.length();
			}

			Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

			if (offset < 0) {
				Log.e(TAG, "readFromFile invalid offset:" + offset);
				return null;
			}
			if (len <= 0) {
				Log.e(TAG, "readFromFile invalid len:" + len);
				return null;
			}
			if (offset + len > (int) file.length()) {
				Log.e(TAG, "readFromFile invalid file len:" + file.length());
				return null;
			}

			byte[] b = null;
			try {
				RandomAccessFile in = new RandomAccessFile(fileName, "r");
				b = new byte[len]; // ´´½¨ºÏÊÊÎÄ¼þ´óÐ¡µÄÊý×é
				in.seek(offset);
				in.readFully(b);
				in.close();

			} catch (Exception e) {
				Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
				e.printStackTrace();
			}
			return b;
		}

		private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

		public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
			Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

			BitmapFactory.Options options = new BitmapFactory.Options();

			try {
				options.inJustDecodeBounds = true;
				Bitmap tmp = BitmapFactory.decodeFile(path, options);
				if (tmp != null) {
					tmp.recycle();
					tmp = null;
				}

				Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
				final double beY = options.outHeight * 1.0 / height;
				final double beX = options.outWidth * 1.0 / width;
				Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
				options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
				if (options.inSampleSize <= 1) {
					options.inSampleSize = 1;
				}

				// NOTE: out of memory error
				while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
					options.inSampleSize++;
				}

				int newHeight = height;
				int newWidth = width;
				if (crop) {
					if (beY > beX) {
						newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
					} else {
						newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
					}
				} else {
					if (beY < beX) {
						newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
					} else {
						newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
					}
				}

				options.inJustDecodeBounds = false;

				Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
				Bitmap bm = BitmapFactory.decodeFile(path, options);
				if (bm == null) {
					Log.e(TAG, "bitmap decode failed");
					return null;
				}

				Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
				final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
				if (scale != null) {
					bm.recycle();
					bm = scale;
				}

				if (crop) {
					final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
					if (cropped == null) { return bm; }

					bm.recycle();
					bm = cropped;
					Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
				}
				return bm;

			} catch (final OutOfMemoryError e) {
				Log.e(TAG, "decode bitmap failed: " + e.getMessage());
				options = null;
			}

			return null;
		}

		public static String sha1(String str) {
			if (str == null || str.length() == 0) { return null; }

			char hexDigits[] =
			{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

			try {
				MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
				mdTemp.update(str.getBytes());

				byte[] md = mdTemp.digest();
				int j = md.length;
				char buf[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
					buf[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(buf);
			} catch (Exception e) {
				return null;
			}
		}

		public static List<String> stringsToList(final String[] src) {
			if (src == null || src.length == 0) { return null; }
			final List<String> result = new ArrayList<String>();
			for (int i = 0; i < src.length; i++) {
				result.add(src[i]);
			}
			return result;
		}

		public abstract interface TaskListener {
			public abstract void onStart();

			public abstract void onFinish();
		}
}
