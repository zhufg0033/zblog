package com.zblog.web.utils;

import com.zblog.sharedb.vo.DataPageVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 翻页帮助类
 *
 */
public class PageUtils {

    public static <T> Page<T> transformPage(DataPageVO<T> dataPageVO){
        Pageable pageable = PageRequest.of(dataPageVO.getPageModuls().getPageNo(),dataPageVO.getPageModuls().getPageSize());
        return new PageImpl<>(dataPageVO.getList(),pageable,dataPageVO.getTotal());
    }

}
