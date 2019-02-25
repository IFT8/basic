package me.ift8.basic.utils;

import me.ift8.basic.utils.model.BeanCopyObj1;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

/**
 * Created by IFT8 on 2019-01-21.
 */
@Slf4j
public class BeanMapperTest {
    @Test
    public void map2Bean1() {

        BeanCopyObj1 demo = BeanCopyObj1.createDemo();
        Map stringObjectMap = BeanMapper.bean2Map(demo);

        BeanCopyObj1 beanCopyObjItem = BeanMapper.map2Bean(stringObjectMap, BeanCopyObj1.class);
        System.out.println(beanCopyObjItem);

    }
}
