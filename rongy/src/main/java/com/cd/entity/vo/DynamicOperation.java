package com.cd.entity.vo;

import java.util.List;

public class DynamicOperation {

    private int isLike;
    private int likeCount;
    private List<DynamicComment> comment;
    private List<DynamicComment> like;

    public DynamicOperation(int isLike, int likeCount, List<DynamicComment> comment, List<DynamicComment> like) {
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.like = like;
        this.comment = comment;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<DynamicComment> getComment() {
        return comment;
    }

    public void setComment(List<DynamicComment> comment) {
        this.comment = comment;
    }

    public List<DynamicComment> getLike() {
        return like;
    }

    public void setLike(List<DynamicComment> like) {
        this.like = like;
    }
}
