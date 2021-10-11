package com.cd.common;

/**
 * @author : jwnming
 */
public enum GoodsBuyTabsEnum {
    // 物品求购标签
    /** 物品求购标签：1-原装机 */
    GOODS_BUY_TAB$1("1", "原装机"),
    /** 物品求购标签：2-支持验货 */
    GOODS_BUY_TAB$2("2", "支持验货"),
    /** 物品求购标签：3-九成新 */
    GOODS_BUY_TAB$3("3", "九成新"),
    /** 物品求购标签：4-有支付凭证 */
    GOODS_BUY_TAB$4("4", "有支付凭证"),
    /** 物品求购标签：5-全新未使用 */
    GOODS_BUY_TAB$5("5", "全新未使用"),
    /** 物品求购标签：6-保修期内 */
    GOODS_BUY_TAB$6("6", "保修期内"),
    ;

    private String key;
    private String value;

    GoodsBuyTabsEnum(String key, String value) {
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
