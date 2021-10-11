package com.cd.utils;

import com.cd.common.MiniappConstants;
import com.cd.entity.miniapp.AccessTokenResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MiniappNotificationUtil {

    private static final Logger logger = LoggerFactory.getLogger(MiniappNotificationUtil.class);

    private String accessTokenRedisKey;

    public String env;

    @Value("${spring.profiles.active}")
    public void setActive(String profile) {
        if (StringUtils.equals(profile, "dev")) {
            env = MiniappConstants.TMPL_ENV_TRAIL;
        } else if (StringUtils.equals(profile, "prod")) {
            env = MiniappConstants.TMPL_ENV_FORMAL;
        } else {
            env = MiniappConstants.TMPL_ENV_DEVELOPER;
        }
        accessTokenRedisKey = profile + "_" + MiniappConstants.ACCESS_TOKEN;
    }



    @Autowired
    private RedisUtil cache;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void onLike(String authorOpenId, String commenterName, String content, int likeCnt) {
        Map<String, Object> data = new HashMap<>();
        data.put("name1", singleMap(commenterName));
        data.put("date2", singleMap(sdf.format(new Date())));
        data.put("thing6", singleMap(content));
        data.put("number4", singleMap(String.valueOf(likeCnt)));

        String page = String.format("%s?%s", MiniappConstants.TMPL_JUMP_PAGE, "from=like_tmpl_msg");
        HttpUtil.sendTmplMsg(getAccessToken(), authorOpenId, MiniappConstants.TMPL_LIKE, data, page, env);
    }

    public void onComment(String authorOpenId, String commenterName, String content, String comment) {
        Map<String, Object> data = new HashMap<>();
        data.put("thing6", singleMap(content));
        data.put("thing5", singleMap(commenterName));
        data.put("thing2", singleMap(comment));
        data.put("time3", singleMap(sdf.format(new Date())));

        String page =  String.format("%s?%s", MiniappConstants.TMPL_JUMP_PAGE, "from=comment_tmpl_msg");;
        HttpUtil.sendTmplMsg(getAccessToken(), authorOpenId, MiniappConstants.TMPL_COMMENT, data, page, env);
    }

    public String getAccessToken() {
        String accessToken = cache.get(accessTokenRedisKey);
        if (accessToken == null) {
            AccessTokenResp resp = HttpUtil.miniAppAccessToken();
            if (resp == null || resp.getErrCode() != MiniappConstants.API_OK) {
                if (resp == null) {
                    logger.error("get access_token error, send failed");
                } else {
                    logger.error("get access_token error: [{}]-[{}]", resp.getErrCode(), resp.getErrMsg());
                }
                return null;
            }

            accessToken = resp.getAccessToken();

            cache.expire(accessTokenRedisKey, resp.getExpiresIn());
        }
        return accessToken;
    }

//    参数类别	            参数说明	参数值限制	                                                    说明
//    thing.DATA	        事物	    20个以内字符	                                                    可汉字、数字、字母或符号组合
//    number.DATA	        数字	    32位以内数字	                                                    只能数字，可带小数
//    letter.DATA	        字母	    32位以内字母	                                                    只能字母
//    symbol.DATA	        符号	    5位以内符号	                                                    只能符号
//    character_string.DATA	字符串	32位以内数字、字母或符号	                                        可数字、字母或符号组合
//    time.DATA	            时间	    24小时制时间格式（支持+年月日），支持填时间段，两个时间点之间用“~”符号连接	例如：15:01，或：2019年10月1日 15:01
//    date.DATA	            日期	    年月日格式（支持+24小时制时间），支持填时间段，两个时间点之间用“~”符号连接	例如：2019年10月1日，或：2019年10月1日 15:01
//    amount.DATA	        金额	    1个币种符号+10位以内纯数字，可带小数，结尾可带“元”	                    可带小数
//    phone_number.DATA	    电话	    17位以内，数字、符号	                                            电话号码，例：+86-0766-66888866
//    car_number.DATA	    车牌	    8位以内，第一位与最后一位可为汉字，其余为字母或数字	                    车牌号码：粤A8Z888挂
//    name.DATA	            姓名	    10个以内纯汉字或20个以内纯字母或符号	                                中文名10个汉字内；纯英文名20个字母内；中文和字母混合按中文名算，10个字内
//    phrase.DATA	        汉字	    5个以内汉字                                                       5个以内纯汉字，例如：配送中

    private Map<String, Object> singleMap(String value) {
        Map<String, Object> map = new HashMap<>();

        // 默认 thing 类型强制截取
        map.put("value", value == null ? "" : value.length() <= 20 ? value : value.substring(0, 17) + "...");

        return map;
    }
}
