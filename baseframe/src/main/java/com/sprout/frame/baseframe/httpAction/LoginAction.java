package com.sprout.frame.baseframe.httpAction;


import com.alibaba.fastjson.JSON;
import com.sprout.frame.baseframe.entity.LoginEntity;
import com.sprout.frame.baseframe.global.Api;
import com.sprout.frame.baseframe.http.HttpAction;
import com.sprout.frame.baseframe.http.HttpResult;
import com.sprout.frame.baseframe.utils.coder.CoderUtil;

import org.json.JSONException;

/**
 * Create by Sprout at 2017/8/15
 * 登录
 */
public class LoginAction extends HttpAction<LoginEntity> {

    private String encryUsername;
    private String encryPassword;

    public LoginAction() {
        super(Api.API_LOGIN);
    }

    public LoginAction para(String username, String password) {
        //加密用户名和密码
        encryUsername = CoderUtil.encode(username);
        encryPassword = CoderUtil.encode(password);
        add("username", encryUsername);
        add("password", encryPassword);
        return this;
    }

    @Override
    public LoginEntity decodeModel(String response, HttpResult<LoginEntity> result) throws JSONException {
        return JSON.parseObject(response, LoginEntity.class);
    }
}
