package me.ift8.basic.db;

/**
 * Created by IFT8 on 2017/6/15.
 */
public enum Order {
    DESC, ASC;

    public String orderBy(String orderField) {
        return orderField + " " + this.name();
    }
}
