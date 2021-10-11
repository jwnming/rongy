package com.cd.entity.vo;

import com.cd.entity.UserInfo;

public class UserLoginVO {

    private UserInfo userInfo;
    private String retFlag;

    public UserLoginVO(UserInfo userInfo, String retFlag) {
        this.userInfo = userInfo;
        this.retFlag = retFlag;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getRetFlag() {
        return retFlag;
    }

    public void setRetFlag(String retFlag) {
        this.retFlag = retFlag;
    }
}
