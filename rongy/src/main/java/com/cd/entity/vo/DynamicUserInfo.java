package com.cd.entity.vo;

public class DynamicUserInfo {

    private String imageUrl;
    private String name;
    private String sex;
    private String userNo;

    public DynamicUserInfo(String imageUrl, String name, String sex, String userNo) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.sex = sex;
        this.userNo = userNo;
    }

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
}
