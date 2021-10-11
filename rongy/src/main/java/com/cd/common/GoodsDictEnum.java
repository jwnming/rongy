package com.cd.common;

public enum GoodsDictEnum {
    // 物品类型枚举值
    /** 物品类型：1-手机数码 */
    GOODSTYPE$1("1", "物品类型：1-手机数码"),
    /** 物品类型：2-书籍资料 */
    GOODSTYPE$2("2", "物品类型：2-书籍资料"),
    /** 物品类型：3-体育户外 */
    GOODSTYPE$3("3", "物品类型：3-体育户外"),
    /** 物品类型：4-生活用品 */
    GOODSTYPE$4("4", "物品类型：4-生活用品"),
    /** 物品类型：5-交通工具 */
    GOODSTYPE$5("5", "物品类型：5-交通工具"),
    /** 物品类型：6-美容保健 */
    GOODSTYPE$6("6", "物品类型：6-美容保健"),
    /** 物品类型：7-服装配饰 */
    GOODSTYPE$7("7", "物品类型：7-服装配饰"),
    /** 物品类型：8-其他 */
    GOODSTYPE$8("8", "物品类型：8-其他"),

    ;

    private String code;
    private String codeMsg;


    GoodsDictEnum(String code, String codeMsg) {
        this.code = code;
        this.codeMsg = codeMsg;
    }


    public String getCode() {
        return code;
    }
    public String getCodeMsg() {
        return codeMsg;
    }
}
