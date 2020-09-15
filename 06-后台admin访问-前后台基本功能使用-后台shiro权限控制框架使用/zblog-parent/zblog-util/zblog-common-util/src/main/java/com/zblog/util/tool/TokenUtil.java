package com.zblog.util.tool;

import java.util.UUID;

public class TokenUtil {

    public static final String token = "token";
    public static final String utoken = "utoken";

    public static String createToken(){
        return UUID.randomUUID().toString();
    }
}
