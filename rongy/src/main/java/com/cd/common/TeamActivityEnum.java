package com.cd.common;

public enum TeamActivityEnum {

    TEAMACTIVITY_STATUS$0("0","邀约中"),
    TEAMACTIVITY_STATUS$1("1","已结束"),
    ;

    private String key;
    private String value;

    TeamActivityEnum(String key, String value) {
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
