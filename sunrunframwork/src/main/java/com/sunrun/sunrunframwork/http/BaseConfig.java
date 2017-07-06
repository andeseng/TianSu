package com.sunrun.sunrunframwork.http;

import android.content.Context;
import android.os.Environment;


import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.app.BaseApplication;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BaseConfig {
	/** app正式签名,用于效验app是否属于正式版 */

	public static  String GLOBAL = "app_global";// app主配置文件名
	public static final String LOGIN_INFO = "login_info";// 登录信息

	private static Map<Object, Object> map = new HashMap<Object, Object>();// 临时的全局数据存放集合

	public static final String IMG_SAVE_DIR = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
			.getAbsoluteFile()
			+ "/image";
	public static final String APK_DOWN=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

	/**
	 * 获取App使用的缓存目录, 外部存储可用时 ,默认使用外部,不可用时,使用内部缓存区域
	 * @param context
	 * @return
	 */
	public static File getAppCacheDir(Context context){
		File cacheDir=null;
		if(Environment.getExternalStorageDirectory().canWrite()){
			cacheDir=new File(Environment.getExternalStorageDirectory(),GLOBAL+"/appcache");
			cacheDir.mkdirs();
		}else {
			cacheDir=new File(context.getCacheDir(),"appcache");
			cacheDir.mkdirs();
		}
		return cacheDir;
	}



	public static  String emojiDir(){
		return new File(BaseApplication.getInstance().getCacheDir().getAbsolutePath(),"emoji").getAbsolutePath();
	}
	/**
	 * 存放登录信息
	 *
	 * @param obj
	 */
	public static void putLoginInfo(Object obj) {
		putDataCache(BaseConfig.LOGIN_INFO, obj);// 保存登录信息
	}



	/**
	 * 存放临时数据 默认不缓存到本地
	 * 
	 * @param key
	 * @param value
	 */
	public static void putData(String key, Object value) {
		putData(key, value, false);
	}

	/**
	 * 缓存临时数据到本地
	 * 
	 * @param key
	 * @param value
	 */
	public static void putDataCache(String key, Object value) {
		putData(key, value, true);
	}

	/**
	 * 存放临时数据,可选择是否缓存到本地 部分数据类型可能无法缓存到本地,请注意
	 */
	public static void putData(String key, Object value, boolean isCacheLocal) {
		map.put(key, value);
		if (isCacheLocal)
			BaseConfig.putConfigInfo(BaseApplication.getInstance(), key,
					JsonDeal.object2Json(value));
	}

	/**
	 * 获取存放的临时数据
	 * 
	 * @param key
	 * @param remove
	 *            是否移除
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getData(String key, boolean remove) {
		return (T) (remove ? map.remove(key) : map.get(key));
	}

	public static <T> T getData(String key) {
		T t = getData(key, false);
		return t;
	}

	/**
	 * 获取缓存的数据,获取之后会自动移除缓存
	 * 
	 * @param key
	 * @param defaultValue
	 *            取不到值时的默认值
	 * @return
	 */
	public static <T> T getData(String key, T defaultValue) {
		T t = getData(key, true);
		return t == null ? defaultValue : t;
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	public static <T> T getDataCache(String key, TypeToken<T> type) {
		T t = getData(key, false);
		if (t == null) {
			t = JsonDeal
					.json2Object(
							getConfigInfo(BaseApplication.getInstance(), key,
									null), type);
			putData(key, t);
		}
		return t;
	}
	public static String getDataCache(String key) {
		String t = getData(key, false);
		if (t == null) {
			t = 
					getConfigInfo(BaseApplication.getInstance(), key,
							null);
			putData(key, t);
		}
		return t;
	}

	/**
	 * 存放全局配置数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putConfigInfo(Context context, String key, String value) {
		save(context, GLOBAL, key, value);
	}
	/**
	 * 获取全局配置数据
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */

	public static String getConfigInfo(Context context, String key,
			String defValue) {
		return load(context, GLOBAL, key, defValue);
	}

	/**
	 * 保存配置数据
	 * 
	 * @param context
	 * @param name
	 *            配置文件名
	 * @param key
	 * @param value
	 */
	public static void save(Context context, String name, String key,
			String value) {
		context.getSharedPreferences(name, Context.MODE_PRIVATE).edit()
		.putString(key, value).commit();
	}

	/**
	 * 获取配置数据
	 * 
	 * @param context
	 * @param name
	 *            配置文件名
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String load(Context context, String name, String key,
			String defValue) {
		return context.getSharedPreferences(name, Context.MODE_PRIVATE)
				.getString(key, defValue);
	}


}
