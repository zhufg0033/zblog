package com.zblog.post.service;

import com.zblog.sharedb.entity.MtoLinks;

import java.util.List;

/**
 * @author : landy
 * @version : 1.0
 * @date : 2019/11/6
 */
public interface LinksService {
    List<MtoLinks> findAll();
    void update(MtoLinks links);
    void delete(long id);
}
