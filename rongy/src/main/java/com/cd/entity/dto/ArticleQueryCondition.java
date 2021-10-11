package com.cd.entity.dto;

public class ArticleQueryCondition {

    private String no;
    private String userNo;
    private String tabbarType;
    private String topicId;
    private int pageSize;
    private int pageNum;


    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getTabbarType() {
        return tabbarType;
    }

    public void setTabbarType(String tabbarType) {
        this.tabbarType = tabbarType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
