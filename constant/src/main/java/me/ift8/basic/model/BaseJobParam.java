package me.ift8.basic.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * Created by IFT8 on 2018/3/5.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseJobParam {
    //默认10d前开始
    Date fromDate = new Date(System.currentTimeMillis() - 864000000);
    Date toDate;
    PageParam pageParam;
}
