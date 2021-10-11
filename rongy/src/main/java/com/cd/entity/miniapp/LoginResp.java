package com.cd.entity.miniapp;

import com.alibaba.fastjson.annotation.JSONField;

public class LoginResp extends BaseResp {

    @JSONField(name = "session_key")
    private String sessionKey;

    @JSONField(name = "openid")
    private String openId;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "LoginResp{" +
                ", sessionKey='" + sessionKey + '\'' +
                ", openId='" + openId + '\'' +
                '}' + "\t" + super.toString();
    }
}
