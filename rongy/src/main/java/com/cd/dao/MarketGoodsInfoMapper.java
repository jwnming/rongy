package com.cd.dao;

import com.cd.entity.MarketGoodsInfo;
import com.cd.entity.RankingList;
import com.cd.entity.dto.GoodsInfoQueryCondition;

import java.util.List;

public interface MarketGoodsInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketGoodsInfo record);

    int insertSelective(MarketGoodsInfo record);

    MarketGoodsInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketGoodsInfo record);

    int updateByPrimaryKeyWithBLOBs(MarketGoodsInfo record);

    int updateByPrimaryKey(MarketGoodsInfo record);

    /**
     * 条件查询
     * @param goodsInfoQC
     * @return
     */
    List<MarketGoodsInfo> selectByQC(GoodsInfoQueryCondition goodsInfoQC);

    /**
     * 根据用户编号查询
     * @param userNo
     * @return
     */
    List<MarketGoodsInfo> selectByUserNo(String userNo);

    /**
     * 浏览量增加1
     * @param goodsInfoId
     * @return
     */
    int viewCountAdd(Integer goodsInfoId);

    // 根据主题id查询
    List<MarketGoodsInfo> selectByTopciId(GoodsInfoQueryCondition goodsInfoQC);

}