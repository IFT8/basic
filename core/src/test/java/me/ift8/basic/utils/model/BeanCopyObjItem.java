package me.ift8.basic.utils.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by IFT8 on 2019-01-21.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BeanCopyObjItem {
    Boolean booleanObjParam;
    boolean booleanParam;
    Integer intObjParam;
    int intParam;
    Long longObjParam;
    long longParam;
    Double doubleObjParam;
    double doubleParam;
    Byte byteObjParam;
    byte byteParam;
    String stringParam;
    byte[] byteArrParam;
    BigDecimal bigDecimalParam;
    Date dateParam;
    LocalDateTime localdateTimeParam;
    Object objParam;

    public static BeanCopyObjItem createDemo() {
        BeanCopyObjItem beanCopyObjItem = new BeanCopyObjItem();
        beanCopyObjItem.setBooleanObjParam(true);
        beanCopyObjItem.setBooleanParam(true);
        beanCopyObjItem.setIntObjParam(-1);
        beanCopyObjItem.setIntParam(-2);
        beanCopyObjItem.setLongObjParam(-1000L);
        beanCopyObjItem.setLongParam(-1001L);
        beanCopyObjItem.setDoubleObjParam(-11.223d);
        beanCopyObjItem.setDoubleParam(-11.224d);
        beanCopyObjItem.setByteObjParam((byte) 12);
        beanCopyObjItem.setByteParam((byte) 13);
        beanCopyObjItem.setStringParam("StringParam");
        beanCopyObjItem.setByteArrParam("ByteArr".getBytes());
        beanCopyObjItem.setBigDecimalParam(new BigDecimal(-1));
        beanCopyObjItem.setDateParam(new Date());
        beanCopyObjItem.setLocaldateTimeParam(LocalDateTime.now());
        beanCopyObjItem.setObjParam(new Object());
        return beanCopyObjItem;
    }
}
