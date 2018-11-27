package me.ift8.basic.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 刘玉雨
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaskUtils {

    /**
     * 手机号打码
     */
    public static String maskMobile(String mobile) {
        return StringUtils.overlay(mobile, "****", 3, 3 + 4);
    }

    public static String maskIdCardNo(String idCardNo) {
        return StringUtils.overlay(idCardNo, "****", 3, 3 + 11);
    }

    public static String maskBankCardNo(String cardNo) {
        if (StringUtils.isEmpty(cardNo)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.overlay(cardNo, "**** **** **** ", 0, cardNo.length() - 4);
    }

    public static String maskShortBankCardNo(String cardNo) {
        return cardNo.substring(cardNo.length() - 4);
    }
}
