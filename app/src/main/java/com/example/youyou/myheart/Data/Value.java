package com.example.youyou.myheart.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youyou on 2018/1/30.
 */

public class Value<T> {
    public ArrayList<T> list;
    public int pageNum;
    public int pageSize;
    public int size;
    public int startRow;
    public int endRow;
    public int total;
    public int pages;
    public int prePage;
    public int nextPage;
    public boolean isFirstPage;
    public boolean isLastPage;
    public boolean hasPreviousPage;
    public boolean hasNextPage;
    public int navigatePages;
    public int[] navigatepageNums;
    public int navigateFirstPage;
    public int navigateLastPage;
    public int lastPage;
    public int firstPage;
}
