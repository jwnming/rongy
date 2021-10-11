package com.cd.enums;

public enum RongyDictEnum {

    // 用户信息 - 性别
    USER_INFO_SEX$MALE("0", "男"),
    USER_INFO_SEX$FEMALE("1", "女"),

    // 用户信息 - 状态
    USER_INFO_STATUS$INVALID("0", "无效"),
    USER_INFO_STATUS$VALID("1", "有效"),

    // 圈子信息 - 状态
    CIRCLE_STATUS$UNCHECKED("00", "未审核"),
    CIRCLE_STATUS$CHECKED("01", "已审核"),
    CIRCLE_STATUS$DELETED("02", "已删除"),

    // 帖子 - 状态
    ARTICLE_STATUS$CHECKED("1", "已审核"),
    ARTICLE_STATUS$DELETED("2", "已删除"),

    // 帖子 - 是否热帖
    ARTICLE_IS_HOT$NO("0", "否"),
    ARTICLE_IS_HOT$YES("1", "是"),

    // 类型 - 权限
    CATEGORY_AUTH$ALL("0", "所有人"),
    CATEGORY_AUTH$ADMIN("1", "管理员N"),

    // 角色信息 - 状态
    ROLE_STATUS$INVALID("0", "无效"),
    ROLE_STATUS$VALID("1", "有效"),

    // 用户圈子关系 - 状态
    USER_CIRCLE_REL_STATUS$UNCHECKED("0", "待审核"),
    USER_CIRCLE_REL_STATUS$CHECKED("1", "已审核"),
    USER_CIRCLE_REL_STATUS$DELETED("2", "已删除"),

    // 主题 - 状态
    TOPIC_STATUS$UNCHECKED("0", "待审核"),
    TOPIC_STATUS$CHECKED("1", "已审核"),
    TOPIC_STATUS$DELETED("2", "已删除"),

    // 评论 - 类型
    COMMENT_TYPE$LIKE("0", "点赞"),
    COMMENT_TYPE$TEXT("1", "文字"),

    // 评论 - 等级
    COMMENT_LEVEL$1("0", "1级"),
    COMMENT_LEVEL$2("1", "2级"),

    // 评论 - 是否已读
    COMMENT_IS_READ$NO("0", "未读"),
    COMMENT_IS_READ$YES("1", "已读"),

    // 粉丝、关注人 查询类型
    FOLLOW_QUERY_TYPE$FOLLOWER("1", "粉丝"),
    FOLLOW_QUERY_TYPE$FOLLOWING("2", "关注"),

    // 我是否关注此用户
    FOLLOW_STATUS$NO("0", "未关注"),
    FOLLOW_STATUS$YES("1", "已关注"),

    // 此用户是否关注我
    FOLLOW_STATUS_TO_ME$NO("0", "未关注"),
    FOLLOW_STATUS_TO_ME$YES("1", "已关注"),

    // 用户角色
    USER_ROLE_NO$ADMIN("1", "管理员"),
    USER_ROLE_NO$COMMON("0", "普通用户"),

    // 登录flag
    USER_LOGIN_FLAG$FALSE("0", "自动登录失败"),
    USER_LOGIN_FLAG$TRUE("1", "自动登录成功"),
    ;
    String code;
    String msg;

    RongyDictEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
