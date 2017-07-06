package com.cnsunrun.tiansu.common.quest;

import com.cnsunrun.tiansu.login.bean.LoginAndRegisterInfo;
import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.http.BaseConfig;
import com.sunrun.sunrunframwork.http.NAction;


/**
 * App配置信息
 */

public class Config extends BaseConfig {

    public static final String START_NUM = "run_num";// 程序启动次数
    public static final String START_IMG = "guide";// 引导页图片路径


    /**
     * 获取登录信息
     *
     * @return 不会为null(没有时返回空的LoginInfo对象)
     */
    public static LoginAndRegisterInfo getLoginAndRegisterInfo() {
        LoginAndRegisterInfo info = getDataCache(BaseConfig.LOGIN_INFO,
                new TypeToken<LoginAndRegisterInfo>() {});
        return info == null ? new LoginAndRegisterInfo() : info;
    }
    public static NAction UserAction() {
        return new NAction().put("user_key", Config.getLoginAndRegisterInfo().getUser_key());
    }

}
