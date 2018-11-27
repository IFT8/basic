package me.ift8.basic.db;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by IFT8 on 2017/5/16.
 * <p>
 * !!!位置不能变动，不能被Spring扫到
 *
 * @author 任齐
 * 在 任齐 版本上进行改动
 */
public interface BaseMapper2<T> extends Mapper<T>, MySqlMapper<T> {

    /**
     * 根据数据对象查询列表
     *
     * @param record 数据对象
     * @return
     */
    default List<T> findAll(T record) {
        return this.select(record);
    }


    default List<T> findAll(T record, String orderField, Order order) {
        Example example = new Example(record.getClass(), false);
        example.setOrderByClause(order.orderBy(orderField));
        example.createCriteria().andEqualTo(record);
        return this.selectByExample(example);
    }

    default List<T> find(T record, String orderField, Order order) {
        Example example = new Example(record.getClass(), false);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(record);
        example.setOrderByClause(order.orderBy(orderField));
        return this.selectByExample(example);
    }

    /**
     * 根据Example条件查询列表
     *
     * @param example Example条件
     * @return
     */
    default List<T> findAll(Example example) {
        if (null != example) {
            return this.selectByExample(example);
        }
        return null;
    }

    /**
     * 根据数据对象查询记录数
     *
     * @param record 数据对象
     * @return
     */
    default int findCount(T record) {
        return this.selectCount(record);
    }

    /**
     * 根据Example条件查询记录数
     *
     * @param example Example条件
     * @return
     */
    default int findCount(Example example) {
        if (null != example) {
            return this.selectCountByExample(example);
        }
        return 0;
    }

    /**
     * 根据主键查询记录
     *
     * @param key 主键 (加@Id属性会被当做主键，和数据库主键并无直接关系)
     * @return
     */
    default T findOneByPrimaryKey(Object key) {
        return this.selectByPrimaryKey(key);
    }

    /**
     * 根据数据对象查询一条记录
     *
     * @param record 数据对象
     * @return
     */
    default T findOne(T record) {
        return this.selectOne(record);
    }

    /**
     * 根据数据对象查询一条记录
     *
     * @param record 数据对象
     */
    default T findLimitOne(T record) {
        RowBounds rowBounds = new RowBounds(0, 1);
        List<T> result = this.selectByRowBounds(record, rowBounds);
        return null != result && !result.isEmpty() ? result.get(0) : null;
    }

    default T findLimitOne(T record, String orderField, Order order) {
        Example example = new Example(record.getClass(), false);
        example.setOrderByClause(order.orderBy(orderField));
        example.createCriteria().andEqualTo(record);

        RowBounds rowBounds = new RowBounds(0, 1);

        List<T> result = this.selectByExampleAndRowBounds(example, rowBounds);
        return null != result && !result.isEmpty() ? result.get(0) : null;
    }

    /**
     * 根据Example条件查询一条数据
     *
     * @param example Example条件
     * @return
     */
    default T findOne(Example example) {
        List<T> list = this.findAll(example);
        return null != list && !list.isEmpty() ? list.get(0) : null;
    }
    /**
     * 根据Example条件查询一条数据
     *
     * @param example Example条件
     * @return
     */
    default T findOnlyOne(Example example) {
        List<T> list = this.findAll(example);
        if(null == list || list.isEmpty()){
            return null;
        }else if(list.size() > 1){
            throw new TooManyResultsException();
        }else{
            return list.get(0);
        }
    }

    /**
     * 根据主键进行更新
     * 只会更新不是null的数据
     */
    default int updateBySelective(T record) {
        return this.updateByPrimaryKeySelective(record);
    }

    /**
     * 批量保存
     *
     * @param list 要保存的数据列表
     * @return
     */
    default int saveBatch(List<T> list) {
        return this.insertList(list);
    }

    /**
     * 分页查询
     */
    default PageInfo<T> findPage(int pageNum, int pageSize, T record) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(this.select(record));
    }

    /**
     * 根据Example条件分页查询
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param example  Example条件
     * @return
     */
    default PageInfo<T> findPage(int pageNum, int pageSize, Example example) {
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = this.selectByExample(example);
        return new PageInfo<>(list);
    }

    default List<T> findByIds(T record, List<Long> ids) {
        Condition condition = new Condition(record.getClass());
        condition.createCriteria().andIn("id", ids);
        return this.findAll(condition);
    }

    /**
     * insertOrUpdate
     */
    @UpdateProvider(type = MysqlMergeProvider.class, method = "dynamicSQL")
    @Options(useCache = false, useGeneratedKeys = false)
    int mergeSelective(T record);
}
