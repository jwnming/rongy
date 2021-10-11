package com.cd.service.impl;

import com.cd.common.GoodsBuyTabsEnum;
import com.cd.common.GoodsSaleTabsEnum;
import com.cd.enums.MarketGoodsEnum;
import com.cd.service.MarketGoodsTabService;
import com.cd.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
public class MarketGoodsTabServiceImpl implements MarketGoodsTabService {
    @Override
    public String getTabsByPublishType(String publishType) throws Exception{

        //获取求购类型的标签
        if(StringUtils.equals(MarketGoodsEnum.TZSC_GOODS_PUBLISH_TYPE$BUY.getKey(), publishType)){
            return CommonUtil.toJson(GoodsBuyTabsEnum.values());
        }else if(StringUtils.equals(MarketGoodsEnum.TZSC_GOODS_PUBLISH_TYPE$SELL.getKey(), publishType)){
            //获取出售类型的标签
            return CommonUtil.toJson(GoodsSaleTabsEnum.values());
        }else{
            return null;
        }
    }
}
