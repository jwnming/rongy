package com.cd.entity.vo;

import com.cd.entity.TopicCls;

public class DynamicListVO {

    private DynamicUserInfo userInfo;
    private DynamicArticle article;
    private DynamicOperation operation;
    private TopicCls topicCls;

    public DynamicListVO(DynamicUserInfo userInfo, DynamicArticle article, DynamicOperation operation) {
        this.userInfo = userInfo;
        this.article = article;
        this.operation = operation;
    }

    public DynamicUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(DynamicUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public DynamicArticle getArticle() {
        return article;
    }

    public void setArticle(DynamicArticle article) {
        this.article = article;
    }

    public DynamicOperation getOperation() {
        return operation;
    }

    public void setOperation(DynamicOperation operation) {
        this.operation = operation;
    }

    public TopicCls getTopicCls() {
        return topicCls;
    }

    public void setTopicCls(TopicCls topicCls) {
        this.topicCls = topicCls;
    }
}
