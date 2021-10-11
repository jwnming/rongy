package com.cd.entity.vo;

import java.util.List;

public class GoodsListVOs {
    private List<GoodsListVO> goodsList;

    private int curPage;
    private int totalPage;

    public GoodsListVOs(List<GoodsListVO> goodsList, int curPage, int totalPage) {
        this.goodsList = goodsList;
        this.curPage = curPage;
        this.totalPage = totalPage;
    }

    public List<GoodsListVO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListVO> goodsList) {
        this.goodsList = goodsList;
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
}
