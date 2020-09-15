/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2019 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.post.service.storage.impl;


import com.zblog.post.service.Storage;
import com.zblog.sharedb.dao.subtreasury.MtoResourceDao;
import com.zblog.sharedb.entity.MtoResource;
import com.zblog.util.conf.SiteOptions;
import com.zblog.util.exception.MtonsException;
import com.zblog.util.tool.FileKit;
import com.zblog.util.tool.FilePathUtils;
import com.zblog.util.tool.ImageUtils;
import com.zblog.util.tool.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author langhsu
 * @since 3.0
 */
public abstract class AbstractStorage implements Storage {

    static Logger log = LoggerFactory.getLogger(AbstractStorage.class);

    @Autowired
    protected SiteOptions options;
    @Autowired
    protected MtoResourceDao mtoResourceDao;

    protected void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new MtonsException("文件不能为空");
        }

        if (!FileKit.checkFileType(file.getOriginalFilename())) {
            throw new MtonsException("文件格式不支持");
        }
    }

    @Override
    public String store(MultipartFile file, String basePath) throws Exception {
        validateFile(file);
        return writeToStore(file.getBytes(), basePath, file.getOriginalFilename());
    }

    @Override
    public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
        validateFile(file);
        byte[] bytes = ImageUtils.scaleByWidth(file, maxWidth);
        return writeToStore(bytes, basePath, file.getOriginalFilename());
    }

    @Override
    public String storeScale(MultipartFile file, String basePath, int width, int height) throws Exception {
        validateFile(file);
        byte[] bytes = ImageUtils.screenshot(file, width, height);
        return writeToStore(bytes, basePath, file.getOriginalFilename());
    }

    public String writeToStore(byte[] bytes, String src, String originalFilename) throws Exception {
        String md5 = MD5.md5File(bytes);
        MtoResource resource = mtoResourceDao.findByMd5(md5);
        if (resource != null){
            return resource.getPath();
        }
        String path = FilePathUtils.wholePathName(src, originalFilename, md5);
        path = writeToStore(bytes, path);

        // 图片入库
        resource = new MtoResource();
        resource.setMd5(md5);
        resource.setPath(path);
        resource.setCreateTime(new Date());
        mtoResourceDao.insert(resource);
        return path;
    }

}
