package com.cd.entity.vo;

import java.util.List;

public class DynamicListVOs {

    private List<DynamicListVO> articles;

    private List<DynamicListVO> stickyArticles;

    private int curPage;
    private int totalPage;

    public DynamicListVOs(List<DynamicListVO> dynamicListVOList, List<DynamicListVO> stickyArticles, int curPage, int totalPage) {
        this.articles = dynamicListVOList;
        this.stickyArticles = stickyArticles;
        this.curPage = curPage;
        this.totalPage = totalPage;
    }

    public List<DynamicListVO> getArticles() {
        return articles;
    }

    public void setArticles(List<DynamicListVO> dynamicListVOList) {
        this.articles = dynamicListVOList;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<DynamicListVO> getStickyArticles() {
        return stickyArticles;
    }

    public void setStickyArticles(List<DynamicListVO> stickyArticles) {
        this.stickyArticles = stickyArticles;
    }
}
