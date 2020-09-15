package com.zblog.post.service;

/**
 * @InterfaceName : StorageFactory  //类名
 * @Description : 文件上传工厂  //描述
 * @Author : zhufugao  //作者
 * @Date: 2020-08-31 14:10  //时间
 */
public interface StorageFactory {
    boolean registry(String key, Storage storage);
    Storage get();
}
