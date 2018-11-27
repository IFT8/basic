package me.ift8.basic.webdriver;

import org.openqa.selenium.Cookie;

import java.util.Date;

/**
 * Created by IFT8 on 2018/6/5.
 */
public class ConstructibleCookie extends Cookie {
    private static final long serialVersionUID = -8626320786014424790L;
    public ConstructibleCookie() {
        super("", "");
    }

    public ConstructibleCookie(String name, String value, String path, Date expiry) {
        super(name, value, path, expiry);
    }

    public ConstructibleCookie(String name, String value, String domain, String path, Date expiry) {
        super(name, value, domain, path, expiry);
    }

    public ConstructibleCookie(String name, String value, String domain, String path, Date expiry, boolean isSecure) {
        super(name, value, domain, path, expiry, isSecure);
    }

    public ConstructibleCookie(String name, String value, String domain, String path, Date expiry, boolean isSecure, boolean isHttpOnly) {
        super(name, value, domain, path, expiry, isSecure, isHttpOnly);
    }

    public ConstructibleCookie(String name, String value) {
        super(name, value);
    }

    public ConstructibleCookie(String name, String value, String path) {
        super(name, value, path);
    }
}
