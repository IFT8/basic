package me.ift8.basic.db.ext;

import me.ift8.basic.db.ext.reflection.Reflections;

import java.util.stream.Stream;

/**
 * @author Frank
 */
public class Weekend<T> extends tk.mybatis.mapper.entity.Example {

    public Weekend(Class<T> entityClass) {
        super(entityClass);
    }

    public Weekend(Class<T> entityClass, boolean exists) {
        super(entityClass, exists);
    }

    public Weekend(Class<T> entityClass, boolean exists, boolean notNull) {
        super(entityClass, exists, notNull);
    }

    @Deprecated
    public WeekendCriteria<T,Object> createCriteriaAddOn() {
        WeekendCriteria<T,Object> weekendCriteria = new WeekendCriteria<>(this.propertyMap,this.exists,this.notNull);
        return weekendCriteria;
    }

    @Override
    public WeekendCriteria<T,Object> createCriteria() {
        return (WeekendCriteria<T,Object>)super.createCriteria();
    }

    @Override
    @Deprecated
    protected Criteria createCriteriaInternal() {
        return this.createCriteriaAddOn();
    }

    public static <A> Weekend<A> of(Class<A> clazz, Boolean exists, boolean notNull){
        return new Weekend<A>(clazz,exists,notNull);
    }
    public static <A> Weekend<A> of(Class<A> clazz, Boolean exists){
        return new Weekend<A>(clazz,exists,Boolean.FALSE);
    }
    public static <A> Weekend<A> of(Class<A> clazz){
        return new Weekend<A>(clazz,Boolean.TRUE);
    }

    @SuppressWarnings("all")
    public WeekendCriteria<T,Object> weekendCriteria(){
        return (WeekendCriteria<T, Object>) this.createCriteria();
    }

    public OrderBy orderBy(Fn<T,?> fn) {
        return super.orderBy(Reflections.fnToFieldName(fn));
    }

    public Weekend<T> selectProperties(Fn<T,?>... fns) {
        super.selectProperties(Stream.of(fns).map(Reflections::fnToFieldName).toArray(String[]::new));
        return this;
    }

    public void countProperty(Fn<T,?> fn) {
        super.setCountProperty(Reflections.fnToFieldName(fn));
    }
}
