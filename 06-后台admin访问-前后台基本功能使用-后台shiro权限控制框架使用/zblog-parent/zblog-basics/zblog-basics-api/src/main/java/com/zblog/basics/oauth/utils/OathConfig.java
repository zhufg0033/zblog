package com.zblog.basics.oauth.utils;


import com.zblog.basics.oauth.APIConfig;

public class OathConfig {
    public static String getValue(String key) {
        return APIConfig.getInstance().getValue(key);
    }
}
