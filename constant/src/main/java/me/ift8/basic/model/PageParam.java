package me.ift8.basic.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by IFT8 on 2018/3/21.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PageParam {
    Integer pageNum = 1;
    Integer pageSize = 10;
}