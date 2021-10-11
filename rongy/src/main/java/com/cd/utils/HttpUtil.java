package com.cd.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.cd.common.MiniappConstants;
import com.cd.entity.miniapp.AccessTokenResp;
import com.cd.entity.miniapp.BaseResp;
import com.cd.entity.miniapp.LoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static LoginResp miniAppLogin(String appId, String appSecret, String jsCode, String grantType) {
        Map<String, String> params = new HashMap<>();

        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", jsCode);
        params.put("grant_type", grantType);

        String httpRes = httpGet(MiniappConstants.LOGIN_URL, params);
        return JSON.parseObject(httpRes, LoginResp.class);
    }

    public static AccessTokenResp miniAppAccessToken() {
        Map<String, String> params = new HashMap<>();

        params.put("appid", MiniappConstants.APP_ID);
        params.put("secret", MiniappConstants.APP_SECRET);
        params.put("grant_type", MiniappConstants.ACCESS_TOKEN_GRANT_TYPE);

        String httpRes = httpGet(MiniappConstants.ACCESS_TOKEN_URL, params);

        return JSON.parseObject(httpRes, AccessTokenResp.class);
    }

    private static String httpGet(String url, Map<String, String> params) {
        try {
            StringBuilder paramBuilder = new StringBuilder();
            params.forEach((key, value) -> paramBuilder.append(key).append("=").append(value).append("&"));

            url = String.format("%s?%s", url, paramBuilder.toString());
            URL connURL = new URL(url);
            logger.info("http get with param: {}", url);

            HttpURLConnection conn = (HttpURLConnection) connURL.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder resBuilder = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                resBuilder.append(str);
            }

            br.close();
            conn.disconnect();

            return resBuilder.toString();
        } catch (IOException e) {
            logger.error("http error:", e);

            return null;
        }
    }

    public static void sendTmplMsg(String accessToken, String openId, String tmplId, Map<String, Object> data, String page, String env) {
        if (accessToken == null) {
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("touser", openId);
        params.put("template_id", tmplId);
        params.put("page", page);
        params.put("data", data);
//        params.put("miniprogram_state", "");
        params.put("lang", "zh_CN");
        params.put("miniprogram_state", env);

        String url = String.format("%s?access_token=%s", MiniappConstants.SEND_TMPL_URL, accessToken);
        String res = httpPost(url, params);

        BaseResp resp = JSON.parseObject(res, BaseResp.class);
        if (resp == null || resp.getErrCode() != MiniappConstants.API_OK) {
            logger.error("send tmpl msg error: {}", resp != null ? resp.getErrCode() + " - " +resp.getErrMsg() : "resp == null, send failed");
        }
    }

    /**
     * 返回true时包含非法信息
     * @param accessToken
     * @param content
     * @return
     */
    public static boolean msgSecCheck(String accessToken, String content) {
        if (accessToken == null) {
            return false;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("content", content);

        String url = String.format("%s?access_token=%s", MiniappConstants.MSG_SEC_CHECK_URL, accessToken);
        String res = httpPost(url, params);

        BaseResp resp = JSON.parseObject(res, BaseResp.class);
        if (resp == null || resp.getErrCode() != MiniappConstants.API_OK) {
            return true;
        }

        return false;
    }

    public static String httpPost(String url, Map<String, Object> params) {
        try {
            String sendString = JSONUtil.toJsonStr(params);
            trustAllHosts();

            URL connURL = new URL(url);
            logger.info("post url: {}, params: {}", url, sendString);

            HttpsURLConnection urlCon = (HttpsURLConnection) connURL.openConnection();

            urlCon.setHostnameVerifier((hostname, session) -> true);
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-type", "application/json;charset=UTF-8");

            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);

            OutputStream os = urlCon.getOutputStream();

            os.write(sendString.getBytes());
            os.flush();

            BufferedReader in = new BufferedReader( new InputStreamReader(urlCon.getInputStream()));
            StringBuilder resBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                resBuilder.append(line);
            }

            return resBuilder.toString();
        } catch (Exception e) {
            logger.error("http post error:", e);

            return null;
        }
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
