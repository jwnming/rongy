package com.cd.entity.vo;

import com.cd.entity.MarketGoodsInfo;
import com.cd.entity.TopicCls;

public class GoodsListVO {
    private DynamicUserInfo userInfo;
    private MarketGoodsInfo goodsInfo;
    private DynamicOperation operation;
    private TopicCls topicCls;

    public GoodsListVO(DynamicUserInfo userInfo, MarketGoodsInfo goodsInfo, DynamicOperation operation) {
        this.userInfo = userInfo;
        this.goodsInfo = goodsInfo;
        this.operation = operation;
    }

    public DynamicUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(DynamicUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public MarketGoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(MarketGoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
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
