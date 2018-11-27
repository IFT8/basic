package me.ift8.basic.utils;

import com.github.pagehelper.PageInfo;
import me.ift8.basic.model.Paging;

import java.util.List;
import java.util.function.Function;

/**
 * 分页构建器，将 PageInfo 或 List 转换为一个分页对象
 *
 * @author biezhi
 * @date 2018/6/26
 * @see Paging
 */
public class PagingBuilder<T> {

    // 分页对象
    private Paging<T> paging;

    /**
     * 根据 List 创建一个分页构建器
     *
     * @param rows list 数据
     * @return
     */
    public static <T> PagingBuilder<T> of(List<T> rows) {
        return of(rows, rows.size());
    }

    /**
     * 根据 List 创建一个分页构建器
     *
     * @param rows  list 数据
     * @param total 总条数
     * @param <T>
     * @return
     */
    public static <T> PagingBuilder<T> of(List<T> rows, int total) {
        PagingBuilder<T> pagingBuilder = new PagingBuilder<>();
        Paging<T>        paging        = new Paging<>();
        paging.setRows(rows);
        paging.setTotal(total);

        pagingBuilder.paging = paging;
        return pagingBuilder;
    }

    /**
     * 将 PageInfo 转换为一个分页构建器
     *
     * @param pageInfo
     * @param <T>
     * @return
     */
    public static <T> PagingBuilder<T> of(PageInfo<T> pageInfo) {
        PagingBuilder<T> pagingBuilder = new PagingBuilder<>();

        Paging<T> paging = new Paging<>();
        paging.setPageNum(pageInfo.getPageNum());
        paging.setPageSize(pageInfo.getPageSize());
        paging.setSize(pageInfo.getSize());
        paging.setStartRow(pageInfo.getStartRow());
        paging.setEndRow(pageInfo.getEndRow());
        paging.setTotal(pageInfo.getTotal());
        paging.setPages(pageInfo.getPages());
        paging.setRows(pageInfo.getList());
        paging.setPrePage(pageInfo.getPrePage());
        paging.setNextPage(pageInfo.getNextPage());
        paging.setIsFirstPage(pageInfo.isIsFirstPage());
        paging.setIsLastPage(pageInfo.isIsLastPage());
        paging.setHasPrevPage(pageInfo.isHasPreviousPage());
        paging.setHasNextPage(pageInfo.isHasNextPage());
        paging.setNavigatePages(pageInfo.getNavigatePages());
        paging.setNavigatePageNumbs(pageInfo.getNavigatepageNums());
        paging.setNavigateFirstPage(pageInfo.getNavigateFirstPage());
        paging.setNavigateLastPage(pageInfo.getNavigateLastPage());
        pagingBuilder.paging = paging;

        return pagingBuilder;
    }

    public PagingBuilder pageSize(int pageSize) {
        this.paging.setPageSize(pageSize);
        return this;
    }

    public PagingBuilder pageNum(int pageNum) {
        this.paging.setPageNum(pageNum);
        return this;
    }

    public PagingBuilder rows(List<T> rows) {
        this.paging.setRows(rows);
        return this;
    }

    public <R> PagingBuilder<R> map(Function<T, R> mapping) {
        PagingBuilder<R> pagingBuilder = new PagingBuilder<>();
        pagingBuilder.paging = this.paging.map(mapping);
        return pagingBuilder;
    }

    public Paging<T> build() {
        return this.paging;
    }

}
