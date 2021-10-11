package com.cd.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DynamicComment {

    private int id;
    private String commentatorName;
    private String becommenteeName;
    private String level;
    private String commentatorId;
    private String becommenteeId;
    private String content;
    private String commentatorAvatar;
    private int targetId;
    private Date createdTime;

    public DynamicComment(int id, String commentatorName, String becommenteeName, String level, String commentatorId, String becommenteeId, String content, String commentatorAvatar, int targetId, Date createdTime) {
        this.commentatorName = commentatorName;
        this.becommenteeName = becommenteeName;
        this.level = level;
        this.commentatorId = commentatorId;
        this.becommenteeId = becommenteeId;
        this.content = content;
        this.commentatorAvatar = commentatorAvatar;
        this.id = id;
        this.targetId = targetId;
        this.createdTime = createdTime;
    }

    public String getCommentatorName() {
        return commentatorName;
    }

    public void setCommentatorName(String commentatorName) {
        this.commentatorName = commentatorName;
    }

    public String getBecommenteeName() {
        return becommenteeName;
    }

    public void setBecommenteeName(String becommenteeName) {
        this.becommenteeName = becommenteeName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(String commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getBecommenteeId() {
        return becommenteeId;
    }

    public void setBecommenteeId(String becommenteeId) {
        this.becommenteeId = becommenteeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentatorAvatar() {
        return commentatorAvatar;
    }

    public void setCommentatorAvatar(String commentatorAvatar) {
        this.commentatorAvatar = commentatorAvatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
