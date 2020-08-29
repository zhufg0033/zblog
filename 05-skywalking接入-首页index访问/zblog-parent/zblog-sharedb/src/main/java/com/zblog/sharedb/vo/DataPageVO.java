package com.zblog.sharedb.vo;

import sun.jvm.hotspot.debugger.Page;

import java.io.Serializable;
import java.util.List;

public class DataPageVO<T> implements Serializable {

    private static final long serialVersionUID = 1071938161731031346L;

    /**
     * 分页属性
     */
    private PageModuls pageModuls;

    /**
     * 对象数据
     */
    private List<T> list;

    /**
     * 总条数
     */
    private long total;

    public DataPageVO(PageModuls pageModuls, List<T> list, long total) {
        this.pageModuls = pageModuls;
        this.list = list;
        this.total = total;
    }

    public PageModuls getPageModuls() {
        return pageModuls;
    }

    public void setPageModuls(PageModuls pageModuls) {
        this.pageModuls = pageModuls;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
