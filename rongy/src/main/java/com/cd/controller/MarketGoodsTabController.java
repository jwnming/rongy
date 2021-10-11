package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.service.MarketGoodsTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/goodsTab")
public class MarketGoodsTabController {
    @Autowired
    private MarketGoodsTabService marketGoodsTabService;


    @GetMapping("/getTabs/{publishType}")
    public RestResult getTabs(@PathVariable("publishType") String publishType) {
        try {
            return ResultUtil.createSuccessResult(marketGoodsTabService.getTabsByPublishType(publishType));
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }
}
