package com.sunrun.sunrunframwork.http;

import com.sunrun.sunrunframwork.bean.BaseBean;

/**
 * Created by WQ on 2017/1/6.
 */

public interface NetResultDispatch {
   public boolean dispatch(final NAction action, BaseBean bean);
}
