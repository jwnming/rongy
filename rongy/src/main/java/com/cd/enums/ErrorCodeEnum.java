package com.cd.enums;

/**
 * @author wanggq
 * @date 
 * @description
 */
public enum ErrorCodeEnum {
    SUCCESS("001","成功"),
    FAILED("002","失败"),
    SIGNATURE_FAILED("003", "验签失败"),

    USER_NO_MATCH("100", "用户不存在"),
    UER_STATUS_INVALID("101", "用户状态为无效"),
    USER_NO_OR_PWD_ERROR("102","编号或密码错误"),
    USER_NOT_ADMIN("103", "管理员权限才能操作"),

    ARTICLE_TITLE_EXISTS("200", "帖子名称已存在"),
    ARTICLE_NOT_EXISTS("201", "帖子不存在"),
    ARTICLE_USER_NOT_MATCH("202", "非发帖人，权限不足"),
    ARTICLE_CONTENT_CONTAINS_SENSITIVE("203", "贴子包含违禁内容"),

    CATEGORY_CAN_NOT_USE_OR_NOT_EXIST("300", "类型不存在或不可用"),

    COMMENT_TYPE_INVALID("400", "评论类别错误"),
    COMMENT_LEVEL_INVALID("401", "评论级别错误"),
    BECOMMENTEE_NOT_EXIST("402", "被评论人不存在"),
    COMMENT_USER_NOT_MATCH("403", "非评论人，权限不足"),
    COMMENT_CONTENT_CONTAINS_SENSITIVE("404", "评论内容含有非法字符"),
    CONTENT_SHOULD_NOT_BE_EMPTY("405", "内容不可为空"),

    FILE_TYPE_NOT_SUPPORT("900", "图片格式不支持"),

    FILE_SYSTEM_ERROR("1000", "文件系统异常"),

    MARKETGOODSDONE2000("2000", "更新商品状态失败"),
    MARKETGOODSDONE2001("2001", "不支持本商品类型"),
    MARKETGOODSDONE2002("2002", "不支持本发布类型"),
    MARKETGOODSDONE2003("2003", "-该必输字段不能为空"),
    MARKETGOODSDONE2004("2004", "金额格式有误"),

    TOPICNOTFOUND3000("3000","主题不存在"),
    TOPICNOTFOUND3001("3001","主题大类必须是动态"),
    TOPICNOTFOUND3002("3002","主题大类必须是跳蚤市场"),
    TOPICNOTFOUND3003("3003","主题小类不匹配"),

    TEAM_TYPE_NOT_SUPPORT("4000","组团活动类型不支持"),

    TEAMACTIVITY0001("TEAMACTIVITY0001", "该活动不存在"),
    TEAMACTIVITY0002("TEAMACTIVITY0002", "报名失败"),
    TEAMACTIVITY0003("TEAMACTIVITY0003", "取消报名失败"),
    TEAMACTIVITY0004("TEAMACTIVITY0004", "已报名，不能重复报名"),
    TEAMACTIVITY0005("TEAMACTIVITY0005", "未报名，不能取消报名"),
    TEAMACTIVITY0006("TEAMACTIVITY0006", "发起人不能报名"),
    TEAMACTIVITY0007("TEAMACTIVITY0007", "发起人不能取消报名"),
    TEAMACTIVITY0008("TEAMACTIVITY0008", "人数已满，报名失败"),
    TEAMACTIVITY0009("TEAMACTIVITY0009", "活动时间不正确，请重新设定"),
    TEAMACTIVITY0010("TEAMACTIVITY0010", "同一天不允许报名两个活动"),
    TEAMACTIVITY0011("TEAMACTIVITY0011", "报名截止时间小于活动时间"),
    TEAMACTIVITY0012("TEAMACTIVITY0012", "错过报名时间"),
    TEAMACTIVITY0013("TEAMACTIVITY0013", "已过报名截止时间，不能取消报名"),
    TEAMACTIVITY0014("TEAMACTIVITY0014", "非发布者无法删除该活动"),
    TEAMACTIVITY0015("TEAMACTIVITY0015", "删除该活动失败"),
    TEAMACTIVITY0016("TEAMACTIVITY0016", "最大人数限制不能超过50人"),
    ;

    ErrorCodeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;

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
