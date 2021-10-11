package com.cd.entity.vo;

import com.cd.entity.UserInfo;

import java.io.Serializable;

/**
 * 用户信息及发帖量
 */
public class RankingListVO implements Serializable {
    private UserInfo userInfo; // 当前用户
    private int articleCount;  // 发帖量 * 10
    private int currentRanking; // 当前排名

    public RankingListVO() {
    }

    public RankingListVO(UserInfo userInfo, int articleCount, int currentRanking) {
        this.userInfo = userInfo;
        this.articleCount = articleCount;
        this.currentRanking = currentRanking;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public int getCurrentRanking() {
        return currentRanking;
    }

    public void setCurrentRanking(int currentRanking) {
        this.currentRanking = currentRanking;
    }
}
