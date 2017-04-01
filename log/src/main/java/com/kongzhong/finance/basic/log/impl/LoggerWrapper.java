package com.kongzhong.finance.basic.log.impl;

import com.kongzhong.finance.basic.log.Logger;
import lombok.Data;
import org.slf4j.Marker;

/**
 * Created by IFT8 on 2017/3/30.
 */
@Data
public class LoggerWrapper implements Logger {

    private org.slf4j.Logger logger;

    public String getName() {
        return logger.getName();
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public void trace(String s) {
        logger.trace(s);
    }

    public void trace(String s, Object o) {
        logger.trace(s, o);
    }

    public void trace(String s, Object o, Object o1) {
        logger.trace(s, o, o1);
    }

    public void trace(String s, Object... objects) {
        logger.trace(s, objects);
    }

    public void trace(String s, Throwable throwable) {
        logger.trace(s, throwable);
    }

    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    public void trace(Marker marker, String s) {
        logger.trace(marker, s);
    }

    public void trace(Marker marker, String s, Object o) {
        logger.trace(marker, s, o);
    }

    public void trace(Marker marker, String s, Object o, Object o1) {
        logger.trace(marker, s, o, o1);
    }

    public void trace(Marker marker, String s, Object... objects) {
        logger.trace(marker, s, objects);
    }

    public void trace(Marker marker, String s, Throwable throwable) {
        logger.trace(marker, s, throwable);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void debug(String s) {
        logger.debug(s);
    }

    public void debug(String s, Object o) {
        logger.debug(s, o);
    }

    public void debug(String s, Object o, Object o1) {
        logger.debug(s, o, o1);
    }

    public void debug(String s, Object... objects) {
        logger.debug(s, objects);
    }

    public void debug(String s, Throwable throwable) {
        logger.debug(s, throwable);
    }

    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    public void debug(Marker marker, String s) {
        logger.debug(marker, s);
    }

    public void debug(Marker marker, String s, Object o) {
        logger.debug(marker, s, o);
    }

    public void debug(Marker marker, String s, Object o, Object o1) {
        logger.debug(marker, s, o, o1);
    }

    public void debug(Marker marker, String s, Object... objects) {
        logger.debug(marker, s, objects);
    }

    public void debug(Marker marker, String s, Throwable throwable) {
        logger.debug(marker, s, throwable);
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public void info(String s) {
        logger.info(s);
    }

    public void info(String s, Object o) {
        logger.info(s, o);
    }

    public void info(String s, Object o, Object o1) {
        logger.info(s, o, o1);
    }

    public void info(String s, Object... objects) {
        logger.info(s, objects);
    }

    public void info(String s, Throwable throwable) {
        logger.info(s, throwable);
    }

    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    public void info(Marker marker, String s) {
        logger.info(marker, s);
    }

    public void info(Marker marker, String s, Object o) {
        logger.info(marker, s, o);
    }

    public void info(Marker marker, String s, Object o, Object o1) {
        logger.info(marker, s, o, o1);
    }

    public void info(Marker marker, String s, Object... objects) {
        logger.info(marker, s, objects);
    }

    public void info(Marker marker, String s, Throwable throwable) {
        logger.info(marker, s, throwable);
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public void warn(String s) {
        logger.warn(s);
    }

    public void warn(String s, Object o) {
        logger.warn(s, o);
    }

    public void warn(String s, Object... objects) {
        logger.warn(s, objects);
    }

    public void warn(String s, Object o, Object o1) {
        logger.warn(s, o, o1);
    }

    public void warn(String s, Throwable throwable) {
        logger.warn(s, throwable);
    }

    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    public void warn(Marker marker, String s) {
        logger.warn(marker, s);
    }

    public void warn(Marker marker, String s, Object o) {
        logger.warn(marker, s, o);
    }

    public void warn(Marker marker, String s, Object o, Object o1) {
        logger.warn(marker, s, o, o1);
    }

    public void warn(Marker marker, String s, Object... objects) {
        logger.warn(marker, s, objects);
    }

    public void warn(Marker marker, String s, Throwable throwable) {
        logger.warn(marker, s, throwable);
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public void error(String s) {
        logger.error(s);
    }

    public void error(String s, Object o) {
        logger.error(s, o);
    }

    public void error(String s, Object o, Object o1) {
        logger.error(s, o, o1);
    }

    public void error(String s, Object... objects) {
        logger.error(s, objects);
    }

    public void error(String s, Throwable throwable) {
        logger.error(s, throwable);
    }

    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    public void error(Marker marker, String s) {
        logger.error(marker, s);
    }

    public void error(Marker marker, String s, Object o) {
        logger.error(marker, s, o);
    }

    public void error(Marker marker, String s, Object o, Object o1) {
        logger.error(marker, s, o, o1);
    }

    public void error(Marker marker, String s, Object... objects) {
        logger.error(marker, s, objects);
    }

    public void error(Marker marker, String s, Throwable throwable) {
        logger.error(marker, s, throwable);
    }

}