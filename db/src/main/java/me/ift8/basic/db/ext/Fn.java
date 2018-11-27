package me.ift8.basic.db.ext;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Frank
 */
public interface Fn<T,R> extends Function<T,R>,Serializable {
}
