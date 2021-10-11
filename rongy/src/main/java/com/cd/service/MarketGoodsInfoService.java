package com.cd.service;

import com.cd.entity.MarketGoodsInfo;
import com.cd.entity.dto.GoodsInfoQueryCondition;
import com.cd.entity.vo.GoodsListVO;
import com.cd.entity.vo.GoodsListVOs;

import java.util.List;

public interface MarketGoodsInfoService {

    /**
     * 查询商品信息类列表
     * @param goodsInfoQC
     * @return
     */
    GoodsListVOs queryAllGoodsInfo(GoodsInfoQueryCondition goodsInfoQC);

    void modifyContentStatus(Integer id, String dealStatus);

    void create(MarketGoodsInfo marketGoodsInfo);

    /**
     * 根据用户编号查询
     * @param userNo
     * @param searchUserNo
     * @return
     */
    List<GoodsListVO> getListByUserNo(String userNo, String searchUserNo);

    /**
     * 根据id及用户编号查询
     * @param goodsId
     * @param userNo
     * @return
     */
    GoodsListVO selectById(int goodsId, String userNo);

    /**
     * 浏览量增加1
     * @param goodsInfoId
     * @return
     */
    int viewCountAdd(Integer goodsInfoId);

    /**
     * 查询商品信息类列表
     * @param goodsInfoQC
     * @return
     */
    GoodsListVOs queryGoodsInfosByTopicId(GoodsInfoQueryCondition goodsInfoQC);

}
