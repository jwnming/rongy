package com.cd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class UserRelationship implements Serializable {
    private Integer id;

    private String userId;

    private String followedUserId;

    private Date createdTime;

    private Date updatedTime;

    private String userNo;

    private String followedUserNo;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(String followedUserId) {
        this.followedUserId = followedUserId == null ? null : followedUserId.trim();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getFollowedUserNo() {
        return followedUserNo;
    }

    public void setFollowedUserNo(String followedUserNo) {
        this.followedUserNo = followedUserNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRelationship that = (UserRelationship) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(followedUserId, that.followedUserId) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedTime, that.updatedTime) &&
                Objects.equals(userNo, that.userNo) &&
                Objects.equals(followedUserNo, that.followedUserNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, followedUserId, createdTime, updatedTime, userNo, followedUserNo);
    }

    @Override
    public String toString() {
        return "UserRelationship{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", followedUserId='" + followedUserId + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", userNo='" + userNo + '\'' +
                ", followedUserNo='" + followedUserNo + '\'' +
                '}';
    }
}