package com.cd.category;

import com.cd.common.GoodsSaleTabsEnum;
import com.cd.enums.MarketGoodsEnum;
import com.cd.utils.CommonUtil;

import java.awt.event.ItemEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    @org.junit.Test
    public void test(){
        Enum<GoodsSaleTabsEnum>[] enums = GoodsSaleTabsEnum.values();
        try {
            System.out.println(CommonUtil.toJson(enums));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        Enum<GoodsSaleTabsEnum>[] enums = GoodsSaleTabsEnum.values();
//        List<String> keys = Arrays.stream(GoodsSaleTabsEnum.values())
//                .map(item -> item.getKey())
//                .collect(Collectors.toList());
//       keys.forEach(ItemEvent -> System.out.println(ItemEvent));
    }
}
