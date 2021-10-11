package com.cd.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Sha1Util {

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";
    private static final Logger log = LoggerFactory.getLogger(Sha1Util.class);

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) {
        byte[] result = null;
        try {
            byte[] data = encryptKey.getBytes(ENCODING);
            // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
            // 生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(MAC_NAME);
            // 用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] text = encryptText.getBytes(ENCODING);
            // 完成 Mac 操作
            result = mac.doFinal(text);

        } catch (Exception e) {
            log.error("加密出现异常", e);
        }
        return  result;
    }


}