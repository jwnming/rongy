package com.cd.entity;

public class RankingList {
    private int articleCount;
    private String userNo;


    public RankingList() {
    }

    public RankingList(int articleCount, String userNo) {
        this.articleCount = articleCount;
        this.userNo = userNo;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
}
