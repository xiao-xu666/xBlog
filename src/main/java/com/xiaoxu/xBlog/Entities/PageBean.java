package com.xiaoxu.xBlog.Entities;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = 5L;

    //总记录数、总页数、当前页码、页面内容（list)
    private int total;
    private int totalPage;
    private int pageSize;
    private List<T> list;
    private int currentPage;
}
