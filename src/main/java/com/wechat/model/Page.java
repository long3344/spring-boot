package com.wechat.model;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/8/31
 */
public class Page {
    private  Integer pageNum=1;
    private  Integer pageSize=15;
    private Integer startRow;
    private boolean count;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }
}
