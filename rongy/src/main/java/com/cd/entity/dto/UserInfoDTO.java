package com.cd.entity.dto;

import java.io.Serializable;

public class UserInfoDTO implements Serializable {
    private String userNo;
    private String introduction;
    private String telephone;

    public UserInfoDTO() {}

    public UserInfoDTO(String userNo, String introduction, String telephone) {
        this.userNo = userNo;
        this.introduction = introduction;
        this.telephone = telephone;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
