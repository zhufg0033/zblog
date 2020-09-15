package com.zblog.util.sharedb;

import com.zblog.util.tool.TokenUtil;
import org.apache.dubbo.rpc.RpcContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HelloWoodes
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();

    private static List<Object> dataSourceKeys = new ArrayList<>();

    public static void setDataSourceKey(DataSourceKey key) {
        CONTEXT_HOLDER.set(key.name());
    }

    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    /**
     * 设置主库-公共库
     * @param
     */
    public static void setMasterDataSourceKey() {
        CONTEXT_HOLDER.set(DataSourceKey.zblog2019.name());
    }

    public static String getDataSourceKey() {
        //先取threadlocal
        String token = CONTEXT_HOLDER.get();
        //取不到，再取filter动态切库
        if(token == null){
            token = RpcContext.getContext().getAttachment(TokenUtil.utoken);
        }
        //最后取不到，返回空
        return token;
    }

    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

    public static List<Object> getDataSourceKeys() {
        return dataSourceKeys;
    }


}