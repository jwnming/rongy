package com.cd.common;

/**
 * @author : jwnming
 */
public enum GoodsSaleTabsEnum {
    // 物品出售标签
    /** 物品出售标签：1-全新未拆封 */
    GOODS_SALE_TAB$1("1", "全新未拆封"),
    /** 物品出售标签：2-专柜正品 */
    GOODS_SALE_TAB$2("2", "专柜正品"),
    /** 物品出售标签：3-支持验货 */
    GOODS_SALE_TAB$3("3", "支持验货"),
    /** 物品出售标签：4-可当面交易 */
    GOODS_SALE_TAB$4("4", "可当面交易"),
    /** 物品出售标签：5-有支付凭证 */
    GOODS_SALE_TAB$5("5", "有支付凭证"),
    /** 物品出售标签：6-包邮 */
    GOODS_SALE_TAB$6("6", "包邮"),
    /** 物品出售标签：7-价格可小刀 */
    GOODS_SALE_TAB$7("7", "价格可小刀"),

    ;

    private String key;
    private String value;

    GoodsSaleTabsEnum(String key, String value) {
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
