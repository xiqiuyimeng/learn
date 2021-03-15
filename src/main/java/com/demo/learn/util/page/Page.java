package com.demo.learn.util.page;

import java.io.Serializable;
import java.util.List;

/**
 * @author luwt
 * @date 2020/4/17.
 */
public class Page<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 9027588550147287672L;

    private int pageSize = 10;    //分页大小
    private int currentPage = 1;    //当前页
    private int currentPageStart;   // 当前页开始的条数
    private int currentPageEnd;     // 当前页结束的条数
    private int total;    //总记录数
    private int totalPage;  // 总页数
    private List<T> result;        //返回对象

    public Page() {
    }

    public Page(int pageNo, int pageSize) {
        this.pageSize = pageSize;
        this.currentPage = pageNo;
    }

    /**
     * 获取总页数
     *
     * @return int
     */
    public int getTotalPage() {
        return total % pageSize == 0 ?
                total / pageSize : total / pageSize + 1;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }


    public int getPageSize() {
        return pageSize;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public int getCurrentPage() {
        return currentPage;
    }


    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPageStart() {
        return (currentPage - 1) * pageSize;
    }

    public void setCurrentPageStart(int currentPageStart) {
        this.currentPageStart = currentPageStart;
    }

    public int getCurrentPageEnd() {
        return Math.min(currentPage * pageSize, total);
    }

    public void setCurrentPageEnd(int currentPageEnd) {
        this.currentPageEnd = currentPageEnd;
    }

    public int getTotal() {
        return total;
    }


    public void setTotal(int total) {
        this.total = total;
    }


    public List<T> getResult() {
        return result;
    }


    public void setResult(List<T> result) {
        this.result = result;
    }

}
