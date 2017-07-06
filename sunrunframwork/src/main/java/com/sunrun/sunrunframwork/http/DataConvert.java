package com.sunrun.sunrunframwork.http;

/**
 * Created by WQ on 2017/2/27.
 */

public interface DataConvert <T>{

    public <T> T convert(NAction action,String json);

}
