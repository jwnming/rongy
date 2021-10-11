package com.cd.common;

public class MiniappConstants {

    /** API 相关 */
    public static final int API_OK = 0;

    public static final String APP_ID = "wx227d9799b59e8f0f";
    public static final String APP_SECRET = "595c9affcd21883ecf53de5ff3dd01a4";

    public static final String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    public static final String LOGIN_GRANT_TYPE = "authorization_code";

    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    public static final String ACCESS_TOKEN_GRANT_TYPE = "client_credential";

    /** 消息模板 */
    public static final String SEND_TMPL_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";

    // 点赞模板
    public static final String TMPL_LIKE = "6VqmvOWXsu6IPrC8IeTq9Eccb6Dz3RIyyzkqHkuNqKM";

    // 评论模板
    public static final String TMPL_COMMENT = "Mx6wxjqdhQ3c7n_7l3ytlZcQAy-cFX7z2uDYERTb8cw";

    public static final String TMPL_JUMP_PAGE = "pages/myMessage/toMyMessage";

    public static final String TMPL_ENV_TRAIL = "trail";

    public static final String TMPL_ENV_FORMAL = "formal";

    public static final String TMPL_ENV_DEVELOPER = "developer";

    /** redis key suffix */
    public static final String ACCESS_TOKEN = "access_token";

    /** 文本校验 */
    public static final String MSG_SEC_CHECK_URL = "https://api.weixin.qq.com/wxa/msg_sec_check";

}
