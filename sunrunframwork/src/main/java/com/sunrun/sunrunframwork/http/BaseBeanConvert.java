package com.sunrun.sunrunframwork.http;

import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;

/**
 * Created by WQ on 2017/2/27.
 */

public class BaseBeanConvert implements DataConvert<BaseBean> {
    @Override
    public  BaseBean convert(NAction action, String json) {
        BaseBean bean ;
        if (action.resultDataType != null)
            bean = JsonDeal.createBean(json, action.resultDataType);
        else if (action.typeToken != null)
            bean = JsonDeal.createBean(json, action.typeToken);
        else
            bean = JsonDeal.createBean(json);
        bean.tag=action.getTag();
        return  bean;
    }
}
