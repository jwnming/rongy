package com.cd.utils;

import com.alibaba.fastjson.JSON;
import java.nio.charset.Charset;

public class JsonSerializer {

    public static final String UTF_8 = "UTF-8";

    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes(Charset.forName(UTF_8));
    }

    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }
}