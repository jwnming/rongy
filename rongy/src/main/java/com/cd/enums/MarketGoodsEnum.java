package com.cd.enums;

public enum MarketGoodsEnum {

    //跳蚤市场发布商品信息操作
    TZSC_GOODS_DEAL_TYPE$FINISHED("001","已完成"),
    TZSC_GOODS_DEAL_TYPE$DELETE("002", "删除"),

    //跳蚤市场发布商品状态
    TZSC_GOODS_STATUS$DOING("1", "活动中"),
    TZSC_GOODS_STATUS$DONE("2", "已完成"),

    DELETE_FLAG$NO("0", "正常"),
    DELETE_FLAG$YES("1", "删除"),

    TZSC_GOODS_PUBLISH_TYPE$BUY("1", "求购"),
    TZSC_GOODS_PUBLISH_TYPE$SELL("2", "出售"),
    ;

    private String key;
    private String value;

    MarketGoodsEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
}
