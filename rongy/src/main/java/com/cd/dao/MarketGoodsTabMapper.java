package com.cd.dao;

import com.cd.entity.MarketGoodsTab;

public interface MarketGoodsTabMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketGoodsTab record);

    int insertSelective(MarketGoodsTab record);

    MarketGoodsTab selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketGoodsTab record);

    int updateByPrimaryKey(MarketGoodsTab record);
}