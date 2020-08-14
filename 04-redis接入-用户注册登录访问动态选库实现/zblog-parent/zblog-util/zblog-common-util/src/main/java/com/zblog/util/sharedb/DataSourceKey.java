package com.zblog.util.sharedb;


import java.util.HashMap;
import java.util.Map;

/**
 * @author HelloWoodes
 */

public enum DataSourceKey {
    /**
     * Order data source key.
     */
    zblog2019,
    /**
     * Storage data source key.
     */
    zblog2020;

    static Map<String,DataSourceKey> map = new HashMap<>();

    static{
        map.put("2019",zblog2019);
        map.put("2020",zblog2020);
    }

    public static DataSourceKey getEnumFromSign(String sign){
        return map.get(sign);
    }
}