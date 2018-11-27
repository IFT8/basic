package me.ift8.basic.model;

import lombok.Data;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 分页对象，PageInfo 的本地版本
 *
 * @author biezhi
 * @date 2018/6/26
 * @see com.github.pagehelper.PageInfo
 */
@Data
public class Paging<T> {

    private int     pageNum;
    private int     pageSize;
    private int     size;
    private int     startRow;
    private int     endRow;
    private long    total;
    private int     pages;
    private List<T> rows;
    private int     prePage;
    private int     nextPage;
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Boolean hasPrevPage;
    private Boolean hasNextPage;

    private int   navigatePages;
    private int[] navigatePageNumbs;
    private int   navigateFirstPage;
    private int   navigateLastPage;

    public <R> Paging<R> map(Function<T, R> mapping) {
        Paging<R> paging = new Paging<>();
        paging.setPageNum(this.pageNum);
        paging.setPageSize(this.pageSize);
        paging.setSize(this.size);
        paging.setStartRow(this.startRow);
        paging.setEndRow(this.endRow);
        paging.setTotal(this.total);
        paging.setPages(this.pages);
        paging.setPrePage(this.prePage);
        paging.setNextPage(this.nextPage);
        paging.setIsFirstPage(this.isFirstPage);
        paging.setIsLastPage(this.isLastPage);
        paging.setHasPrevPage(this.hasPrevPage);
        paging.setHasNextPage(this.hasNextPage);
        paging.setNavigatePages(this.navigatePages);
        paging.setNavigatePageNumbs(this.navigatePageNumbs);
        paging.setNavigateFirstPage(this.navigateFirstPage);
        paging.setNavigateLastPage(this.navigateLastPage);

        List<R> rows = this.rows.stream().map(mapping).collect(toList());
        paging.setRows(rows);
        return paging;
    }

}
