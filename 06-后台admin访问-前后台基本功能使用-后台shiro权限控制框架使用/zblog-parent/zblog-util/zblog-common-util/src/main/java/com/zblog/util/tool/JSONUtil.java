package com.zblog.util.tool;

import com.alibaba.fastjson.JSON;

/**
 * JSON 工具类
 *
 * 封装一层 方便以后换JSON对象
 */
public class JSONUtil {

    public static  <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    public static String toJSONString(Object javaObject) {
        return JSON.toJSONString(javaObject);
    }

    public static byte[] toJSONBytes(Object javaObject) {
        return JSON.toJSONBytes(javaObject);
    }

}
