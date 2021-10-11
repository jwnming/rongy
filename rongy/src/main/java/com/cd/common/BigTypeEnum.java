package com.cd.common;

public enum BigTypeEnum {

    BIGTYPE$0("0","动态"),
    BIGTYPE$1("1","跳蚤市场"),


    ;

    private String key;
    private String value;

    BigTypeEnum(String key, String value) {
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
