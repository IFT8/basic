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
public class BeanCopyObj2 {
    Boolean booleanParam;
    Integer intParam;
    Long longParam;
    Double doubleParam;
    Byte byteParam;
    String stringParam;
    Byte[] byteArrParam;
    BigDecimal bigDecimalParam;
    Date dateParam;
    LocalDateTime localdateTimeParam;
    Object objParam;
    List<String> strList;
    List<Integer> intList;
    List<BeanCopyObjItem> objList;

    public static BeanCopyObj2 createDemo() {
        BeanCopyObj2 beanCopyObj = new BeanCopyObj2();
        beanCopyObj.setIntParam(-2);
        beanCopyObj.setLongParam(-1001L);
        beanCopyObj.setDoubleParam(-11.224d);
        beanCopyObj.setByteParam((byte) 13);
        beanCopyObj.setStringParam("StringParam");

        byte[] bytes1 = "ByteArr".getBytes();
        Byte[] bytes = new Byte[bytes1.length];
        for (int i = 0; i < bytes1.length; i++) {
            bytes[i] = bytes1[i];
        }

        beanCopyObj.setByteArrParam(bytes);
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
