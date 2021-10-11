package com.cd.entity.dto;

public class CommentDTO {

    private int articleId;
    private String content;
    private String type;
    private String becommenteeId;
    private String commentatorId;
    private String level;
    private int targetId;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBecommenteeId() {
        return becommenteeId;
    }

    public void setBecommenteeId(String becommenteeId) {
        this.becommenteeId = becommenteeId;
    }

    public String getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(String commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
}
