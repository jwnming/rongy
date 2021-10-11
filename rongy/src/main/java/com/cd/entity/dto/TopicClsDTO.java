package com.cd.entity.dto;

import java.util.List;

public class TopicClsDTO {

    private String bigType;

    private String smallType;

    private String title;

    private List<String> imageUrl;

    private List<String> vadioUrl;

    private String content;

    private String userNo;

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }

    public String getSmallType() {
        return smallType;
    }

    public void setSmallType(String smallType) {
        this.smallType = smallType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getVadioUrl() {
        return vadioUrl;
    }

    public void setVadioUrl(List<String> vadioUrl) {
        this.vadioUrl = vadioUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
}
