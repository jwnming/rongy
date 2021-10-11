package com.cd.entity.vo;

import com.cd.entity.UserInfo;

public class UserInfoVO {

    private UserInfo userInfo;
    private MyOperationVO operation;
    private int unreadCount;

    public UserInfoVO(UserInfo userInfo, MyOperationVO operation, int unreadCount) {
        this.userInfo = userInfo;
        this.operation = operation;
        this.unreadCount = unreadCount;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public MyOperationVO getOperation() {
        return operation;
    }

    public void setOperation(MyOperationVO operation) {
        this.operation = operation;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
