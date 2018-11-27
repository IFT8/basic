package me.ift8.basic.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created by IFT8 on 2017/5/15.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDUtils {

    public static String generateUUID() {
        String uuidStr = UUID.randomUUID().toString();
        // 去掉"-"符号
        return uuidStr.replaceAll("-", "");
    }
}
