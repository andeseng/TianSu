package com.cnsunrun.tiansu.login.bean;

import android.support.annotation.IntDef;

import com.sunrun.sunrunframwork.utils.EmptyDeal;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cnsunrun on 2017/3/21.
 * <p>
 * 登录注册模型类
 */

public class LoginAndRegisterInfo implements Serializable {
    /**
     * 用户类型 10:用户 20:客服 30：专家 40：管理员
     */
    public static final int USER_TYPE_USER = 10;
    public static final int USER_TYPE_SERVICE = 20;
    public static final int USER_TYPE_EXPERTS = 30;
    public static final int USER_TYPE_ADMIN = 40;

    @IntDef({USER_TYPE_USER, USER_TYPE_SERVICE, USER_TYPE_EXPERTS, USER_TYPE_ADMIN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserType {
    }

    public static final long serialVersionUID = 1180329787328184786L;

    /**
     * user_key : MDAwMDAwMDAwMH-Omdl_e8_dhc2csIG5jJiP3bNthaVtoA
     * is_mobile : 0
     * is_consent : 0
     * is_question : 0
     * is_extract : 1
     */

    private String user_key;
    private String is_mobile;
    private String is_consent;
    private String is_question;
    private String is_extract;
    private String user_id;
    private String r_token; //融云的token

    public String getR_token() {
        return r_token;
    }

    public void setR_token(String r_token) {
        this.r_token = r_token;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getIs_mobile() {
        return is_mobile;
    }

    public void setIs_mobile(String is_mobile) {
        this.is_mobile = is_mobile;
    }

    public String getIs_consent() {
        return is_consent;
    }

    public void setIs_consent(String is_consent) {
        this.is_consent = is_consent;
    }

    public String getIs_question() {
        return is_question;
    }

    public void setIs_question(String is_question) {
        this.is_question = is_question;
    }

    public String getIs_extract() {
        return is_extract;
    }

    public void setIs_extract(String is_extract) {
        this.is_extract = is_extract;
    }

    public boolean isValid() {
        return !EmptyDeal.isEmpy(user_key);
    }
}


