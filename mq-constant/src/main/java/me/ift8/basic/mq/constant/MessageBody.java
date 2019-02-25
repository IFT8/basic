package me.ift8.basic.mq.constant;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by IFT8 on 2017/12/19.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageBody<T> {
    MessageContext context;
    T data;
}
