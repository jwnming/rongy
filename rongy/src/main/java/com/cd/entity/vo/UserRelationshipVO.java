package com.cd.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserRelationshipVO {

    private String imageUrl;
    private String name;
    private String introduction;
    private String userNo;
    private String status;
    private String statusToMe;
    private Date time;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusToMe() {
        return statusToMe;
    }

    public void setStatusToMe(String statusToMe) {
        this.statusToMe = statusToMe;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
