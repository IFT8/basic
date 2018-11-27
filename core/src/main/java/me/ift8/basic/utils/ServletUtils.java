package me.ift8.basic.utils;

import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * @author 刘玉雨
 * @version 1.0
 */
public class ServletUtils {
    //-- Content Type 定义 --//
    public static final String TEXT_TYPE = "text/plain";
    public static final String JSON_TYPE = "application/json";
    public static final String XML_TYPE = "text/xml";
    public static final String HTML_TYPE = "text/html";
    public static final String JS_TYPE = "text/javascript";
    public static final String EXCEL_TYPE = "application/vnd.ms-excel";
    //-- Header 定义 --//
    public static final String AUTHENTICATION_HEADER = "Authorization";
    //-- 常用数值定义 --//
    public static final Integer ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;
    /**
     * 设置客户端缓存过期时间 的Header.
     */
    public static void setExpiresHeader(HttpServletResponse response, Integer expiresSeconds) {
        //Http 1.0 header
        response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
        //Http 1.1 header
        response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
    }
    /**
     * 设置禁止客户端缓存的Header.
     */
    public static void setDisableCacheHeader(HttpServletResponse response) {
        //Http 1.0 header
        response.setDateHeader("Expires", 1L);
        response.addHeader("Pragma", "no-cache");
        //Http 1.1 header
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    }
    /**
     * 设置LastModified Header.
     */
    public static void setLastModifiedHeader(HttpServletResponse response, Integer lastModifiedDate) {
        response.setDateHeader("Last-Modified", lastModifiedDate);
    }
    /**
     * 设置Etag Header.
     */
    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader("ETag", etag);
    }
    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     * <p/>
     * 如果无修改, checkIfModify返回false ,设置304 not modify status.
     *
     * @param lastModified 内容的最后修改时间.
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
                                               Integer lastModified) {
        Long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        return true;
    }
    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     * <p/>
     * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
     *
     * @param etag 内容的ETag.
     */
    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }
            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader("ETag", etag);
                return false;
            }
        }
        return true;
    }
    /**
     * 设置让浏览器弹出下载对话框的Header.
     *
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        try {
            //中文文件名支持
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
        }
    }
    /**
     * 取得带相同前缀的Request Parameters.
     * <p/>
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings({"rawtypes"})
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }
    /**
     * 判断是否Ajax请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        String xhrHeader = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(xhrHeader);
    }
    public static boolean isPage(HttpServletRequest request){
        //自定义头的处理，带此HTTP头的请求视为普通请求
        String handleWith = request.getHeader("HANDLE_WITH");
        return !(handleWith != null && "HTML".equalsIgnoreCase(handleWith));
    }
    /**
     * 获取浏览器缓存的cookie
     *
     * @param cookieName cookie名称
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Assert.notNull(request);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String foundCookieName = cookie.getName();
                if (foundCookieName.equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    /**
     * 获取HTTP 头
     *
     * @param headerName HTTP 头 名称
     */
    public static String getHeader(String headerName, HttpServletRequest request) {
        return request.getHeader(headerName);
    }
    /**
     * 获取Referrer（上个页面）
     *
     */
    public static String getReferrer(HttpServletRequest request){
        return getHeader("Referrer", request);
    }
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    /**
     * 真实路径
     */
    public static String getRealPath(HttpServletRequest request, String uri){
        return request.getServletContext().getRealPath(uri);
    }
    public static <T> T getAttribute(String attrName,ServletRequest request){
        return (T)request.getAttribute(attrName);
    }
    public static void printAttribute(HttpServletRequest request){
        Enumeration<String> names = request.getAttributeNames();
        while (names.hasMoreElements()){
            String name = names.nextElement();
            Object value = request.getAttribute(name);
            System.out.println(name + ":" + value);
        }
        System.out.println("------------------------------------------->");
    }
}
