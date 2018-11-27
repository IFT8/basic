package me.ift8.basic.db.ext;

import me.ift8.basic.db.ext.reflection.Reflections;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Map;

/**
 * @author Frank
 */
public class WeekendCriteria<A,B> extends Criteria {
    protected WeekendCriteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
        super(propertyMap, exists, notNull);
    }

    public WeekendCriteria<A, B> andIsNull(Fn<A,B> fn){
        this.andIsNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public WeekendCriteria<A,B> andIsNotNull(Fn<A,B> fn) {
        super.andIsNotNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public WeekendCriteria<A, B> andEqualTo(Fn<A,B> fn, Object value) {
        super.andEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A,B> andNotEqualTo(Fn<A,B> fn, Object value) {
         super.andNotEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A,B> andGreaterThan(Fn<A,B> fn, Object value) {
         super.andGreaterThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A,B> andGreaterThanOrEqualTo(Fn<A,B> fn, Object value) {
         super.andGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A,B> andLessThan(Fn<A,B> fn, Object value) {
         super.andLessThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A,B> andLessThanOrEqualTo(Fn<A,B> fn, Object value) {
         super.andLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A,B> andIn(Fn<A,B> fn, Iterable values) {
        super.andIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public WeekendCriteria<A,B> andNotIn(Fn<A,B> fn, Iterable values) {
         super.andNotIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public WeekendCriteria<A,B> andBetween(Fn<A,B> fn, Object value1, Object value2) {
        super.andBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public WeekendCriteria<A,B> andNotBetween(Fn<A,B> fn, Object value1, Object value2) {
         super.andNotBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public WeekendCriteria<A,B> andLike(Fn<A,B> fn, String value) {
         super.andLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A,B> andNotLike(Fn<A,B> fn, String value) {
        super.andNotLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

}
