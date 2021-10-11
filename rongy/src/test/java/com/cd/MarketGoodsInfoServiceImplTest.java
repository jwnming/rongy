package com.cd;

import com.cd.entity.MarketGoodsInfo;
import com.cd.service.MarketGoodsInfoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketGoodsInfoServiceImplTest {
    @Autowired
    private MarketGoodsInfoService marketGoodsInfoService;

    @Test
    void testCreate() {
        MarketGoodsInfo marketGoodsInfo = new MarketGoodsInfo();
        marketGoodsInfo.setCategoryId(1);
        marketGoodsInfo.setType("1");
        marketGoodsInfo.setPrice(BigDecimal.TEN);
        marketGoodsInfo.setUserNo("oEHh55LlrKD4AwMpTG7MRdRbqhQw");
        marketGoodsInfo.setContent("便宜卖");
//        marketGoodsInfo.setImageUrls();

        marketGoodsInfoService.create(marketGoodsInfo);
        System.out.println(marketGoodsInfo.getId());
    }
}
