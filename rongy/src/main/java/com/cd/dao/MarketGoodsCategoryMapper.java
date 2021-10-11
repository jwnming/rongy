package com.cd.dao;

import com.cd.entity.MarketGoodsCategory;

public interface MarketGoodsCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketGoodsCategory record);

    int insertSelective(MarketGoodsCategory record);

    MarketGoodsCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketGoodsCategory record);

    int updateByPrimaryKey(MarketGoodsCategory record);
}