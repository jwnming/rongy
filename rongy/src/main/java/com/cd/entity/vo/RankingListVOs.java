package com.cd.entity.vo;

import com.cd.entity.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 排行榜
 */
public class RankingListVOs implements Serializable {
    private RankingListVO currentUser;  // 当前用户
    private List<RankingListVO> rankingList; // 所有用户

    public RankingListVOs() {

    }

    public RankingListVOs(RankingListVO currentUser, List<RankingListVO> rankingList) {
        this.currentUser = currentUser;
        this.rankingList = rankingList;
    }

    public RankingListVO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(RankingListVO currentUser) {
        this.currentUser = currentUser;
    }

    public List<RankingListVO> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<RankingListVO> rankingList) {
        this.rankingList = rankingList;
    }
}

