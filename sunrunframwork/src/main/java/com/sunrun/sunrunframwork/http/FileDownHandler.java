package com.sunrun.sunrunframwork.http;

import java.io.File;
import java.io.IOException;

import org.apache.http.Header;

import android.content.Context;

import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

/**
 * 文件请求handler 简单封装了一个静态方法
 * @author WQ 下午4:56:27
 */
public class FileDownHandler extends RangeFileAsyncHttpResponseHandler {
	File saveFile,tmpFile;
	public FileDownHandler(Context context,String filename) {
		super(getCacheDownFile(context, filename));
		saveFile=getCacheDownFile(context, filename);
		tmpFile=new File(saveFile.getParentFile(),"tmp/"+filename);
	} 
	
	public File getFilePath(){
		return saveFile;
	}
	@Override
	public void onFailure(int status, Header[] arg1, Throwable arg2, File arg3) {
		
	}
	
	
	@Override
	final public void onSuccess(int status, Header[] arg1, File file) {
		if(!tmpFile.getParentFile().exists()){
			tmpFile.getParentFile().mkdirs();
		}
		try {
			tmpFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		onSuccess(file);
	}

	public void onSuccess( File file) {
		
	}
	public static boolean isExists(Context context,String filename){
	return getCacheDownFile(context,filename).exists();	
	}
	
	public static File getCacheDownFile(Context context,String filename){
		return new File(BaseConfig.getAppCacheDir(context), filename);
	}
	/**
	 * @param context
	 * @param action
	 * @param fileHandler 
	 * @param isOverride  是否覆盖
	 * @return
	 */
	public static RequestHandle  startDown(Context context,NAction action,FileDownHandler fileHandler,boolean isOverride){
		RequestHandle handler=null;
		if(isOverride){
			fileHandler.getFilePath().delete();
			fileHandler.tmpFile.delete();
		}
		if(fileHandler.tmpFile!=null && fileHandler.tmpFile.exists() && fileHandler.getFilePath().exists() ){
			fileHandler.onSuccess(fileHandler.getFilePath());
			return null;
		} 
		if(action.requestType ==NetRequestHandler.GET){
			handler=NetUtils.doGet(action.url,action.params, fileHandler); 
		}else {
			handler=NetUtils.doPost(action.url,action.params, fileHandler); 
		}
		
		return handler;
	}

}
