package com.zblog.sharedb.vo;

import java.io.Serializable;

/**
 * zhufg
 * 翻页属性对象
 */
public class PageModuls implements Serializable {
    private int pageNo = 1;//初始化第一页
    private int pageSize = 8;//初始化条数
    private String sort;//默认排序空  赋值规则    column sort  例  id desc

    public PageModuls() {
    }

    public PageModuls(int pageNo, int pageSize, String sort) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public PageModuls(String sort) {
        this.sort = sort;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
