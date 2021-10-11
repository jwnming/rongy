package com.cd.entity.dto;

import com.cd.entity.TopicCls;
import com.cd.entity.UserInfo;

import java.util.List;

public class TopicDTO {

    private TopicCls topicCls; // 主题信息

    private List<UserInfo> userInfos; // 参与的人数

    private int articleCount; // 帖子数量

    private Object articles; // 跟帖列表

    public TopicCls getTopicCls() {
        return topicCls;
    }

    public void setTopicCls(TopicCls topicCls) {
        this.topicCls = topicCls;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public Object getArticles() {
        return articles;
    }

    public void setArticles(Object articles) {
        this.articles = articles;
    }
}
