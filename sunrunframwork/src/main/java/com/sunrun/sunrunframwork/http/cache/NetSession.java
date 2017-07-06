package com.sunrun.sunrunframwork.http.cache;

import java.io.Serializable;

import android.content.Context;
import android.support.v4.util.LruCache;

import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;

/**
 * 网络缓存
 * @author cnsunrun
 */
public class NetSession {
	static NetSession _session;
	ACache acache;//磁盘文件缓存
	LruCache<String ,Object >memoryCachel;//内存缓存
	private NetSession(Context context) {
		acache = ACache.get(context);
		memoryCachel=new LruCache<>(500);
	};

	public static NetSession instance(Context context) {
		if (_session == null)
			synchronized (NetSession.class) {
				if (_session == null)
					_session = new NetSession(context);
			}
		return _session;
	}



	public <T> T getObject(String key, Class<T> clazz) {
		Object object =getMemoryCache(key);
		if(object!=null){
			return (T) object;
		}
		return JsonDeal.json2Object(acache.getAsString(key), clazz);
	}

	Object getMemoryCache(String key){
		return memoryCachel.get(key);
	}

	public <T> T getBean(String key, TypeToken<T> type) {
		Object object =getMemoryCache(key);
		if(object!=null){
			if( object instanceof String){
				return	JsonDeal.json2Object(String.valueOf(object), type);
			}
			return (T) object;
		}
		return JsonDeal.json2Object(acache.getAsString(key), type);
	}

	public int getInt(String key) {
		Object object =getMemoryCache(key);
		if(object!=null){
			return (int) object;
		}
		Number value = (Number) acache.getAsObject(key);
		return value == null ? 0 : value.intValue();
	}

	public float getFloat(String key) {
		Object object =getMemoryCache(key);
		if(object!=null){
			return (float) object;
		}
		Number value = (Number) acache.getAsObject(key);
		return value == null ? 0 : value.floatValue();
	}
	public long getLong(String key){
		Object object =getMemoryCache(key);
		if(object!=null){
			return (long) object;
		}
		Number value = (Number) acache.getAsObject(key);
		return value == null ? 0 : value.longValue();
	}

	public double getDouble(String key) {
		Object object =getMemoryCache(key);
		if(object!=null){
			return (double) object;
		}
		Number value = (Number) acache.getAsObject(key);
		return value == null ? 0 : value.doubleValue();
	}

	public String getString(String key) {
		Object object =getMemoryCache(key);
		if(object!=null){
			return (String) object;
		}
		return acache.getAsString(key);
	}

	public boolean getBoolean(String key) {
		Object object =getMemoryCache(key);
		if(object!=null){
			return (boolean) object;
		}
		Boolean value = (Boolean) acache.getAsObject(key);
		return value == null ? false : value.booleanValue();
	}

	public void remove(String key) {
		memoryCachel.remove(key);
		acache.remove(key);
	}

	public void removeAll() {
		memoryCachel.evictAll();
		acache.clear();
	}

	public boolean hasValue(String key) {
		Object object =getMemoryCache(key);
		if(object!=null){
			return true;
		}
		return acache.file(key) != null;
	}

	public void put(String key, Object value) {
		if(value!=null){
			memoryCachel.put(key,value);
		}else {
			memoryCachel.remove(key);
		}
		if (value instanceof Number || value instanceof Boolean) {
			acache.put(key, (Serializable) value);
		} else if (value instanceof String) {

			acache.put(key, String.valueOf(value));
		} else {
			acache.put(key, JsonDeal.object2Json(value));
		}
	}

}
