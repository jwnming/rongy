package com.cd.entity.miniapp;

import com.alibaba.fastjson.annotation.JSONField;

public class BaseResp {

    @JSONField(name = "errcode")
    private int errCode;

    @JSONField(name = "errmsg")
    private String errMsg;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
