package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.MarketGoodsInfo;
import com.cd.entity.dto.GoodsInfoQueryCondition;
import com.cd.entity.vo.GoodsListVO;
import com.cd.entity.vo.GoodsListVOs;
import com.cd.service.MarketGoodsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class MarketGoodsInfoController {

    @Autowired
    private MarketGoodsInfoService marketGoodsInfoService;

    /**
     * 获取列表
     * @param goodsInfoQC
     * @return
     */
    @GetMapping("/goodsInfos")
    public RestResult list(GoodsInfoQueryCondition goodsInfoQC) {
        GoodsListVOs vos = marketGoodsInfoService.queryAllGoodsInfo(goodsInfoQC);

        return ResultUtil.createSuccessResult(vos);
    }

    @PostMapping("/goodsInfoContentDone/{id}/{dealType}")
    public RestResult goodInfoContentDone(HttpServletRequest request, @PathVariable("id") int id, @PathVariable("dealType") String dealType ) {
        try {
            marketGoodsInfoService.modifyContentStatus(id, dealType);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @PostMapping("/publishGoods")
    public RestResult publishGoods(HttpServletRequest request, @RequestBody MarketGoodsInfo marketGoodsInfo){
        try {
            marketGoodsInfoService.create(marketGoodsInfo);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    /**
     * 一个用户查询另一个用户的所有商品
     */
    @GetMapping("/{userNo}/goodsInfos")
    public RestResult listByUserNo(@PathVariable("userNo") String userNo, @RequestParam("searchUserNo") String searchUserNo) {
        List<GoodsListVO> vos = marketGoodsInfoService.getListByUserNo(userNo, searchUserNo);
        return ResultUtil.createSuccessResult(vos);
    }

    /**
     * 查询详情
     */
    @GetMapping("/getGoodsById/{id}")
    public RestResult get(@PathVariable("id") int id, @RequestParam("userNo") String userNo) {
        GoodsListVO vo = marketGoodsInfoService.selectById(id, userNo);
        return ResultUtil.createSuccessResult(vo);
    }

    /**
     * 浏览量增加1
     * @param goodsInfoId
     * @return
     */
    @GetMapping("/viewCountAdd/{goodsInfoId}")
    public RestResult viewCountAdd(@PathVariable("goodsInfoId") Integer goodsInfoId) {
        int i = marketGoodsInfoService.viewCountAdd(goodsInfoId);

        return i == 1 ? ResultUtil.createSuccessResult() : ResultUtil.createFailedResult();
    }

}
