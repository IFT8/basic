package me.ift8.basic.utils.model;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by IFT8 on 2019-01-21.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BeanCopyObj1 {
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

    List<String> strList;
    List<Integer> intList;
    List<BeanCopyObjItem> objList;

    public static BeanCopyObj1 createDemo() {
        BeanCopyObj1 beanCopyObj = new BeanCopyObj1();
        beanCopyObj.setBooleanObjParam(true);
        beanCopyObj.setBooleanParam(true);
        beanCopyObj.setIntObjParam(-1);
        beanCopyObj.setIntParam(-2);
        beanCopyObj.setLongObjParam(-1000L);
        beanCopyObj.setLongParam(-1001L);
        beanCopyObj.setDoubleObjParam(-11.223d);
        beanCopyObj.setDoubleParam(-11.224d);
        beanCopyObj.setByteObjParam((byte) 12);
        beanCopyObj.setByteParam((byte) 13);
        beanCopyObj.setStringParam("StringParam");
        beanCopyObj.setByteArrParam("ByteArr".getBytes());
        beanCopyObj.setBigDecimalParam(new BigDecimal(-1));
        beanCopyObj.setDateParam(new Date());
        beanCopyObj.setLocaldateTimeParam(LocalDateTime.now());
        beanCopyObj.setObjParam(new Object());
        beanCopyObj.setStrList(Lists.newArrayList("str1", "str2", "str3"));
        beanCopyObj.setIntList(Lists.newArrayList(1, 2, 3));

        beanCopyObj.setObjList(Lists.newArrayList(BeanCopyObjItem.createDemo(), BeanCopyObjItem.createDemo()));
        return beanCopyObj;
    }
}
