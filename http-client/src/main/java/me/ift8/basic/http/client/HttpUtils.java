package me.ift8.basic.http.client;

import me.ift8.basic.exception.SystemException;
import me.ift8.basic.utils.JsonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IFT8 on 2017/4/6.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {
    private static final String APPLICATION_JSON = "application/json";

    public static String doPost(CloseableHttpClient httpClient, String url, Map<String, String> paramsMap) {
        return doPost(httpClient, url, paramsMap, null, null, null, null);
    }

    public static String doPost(CloseableHttpClient httpClient, String url, Map<String, String> paramsMap, boolean checkResponseStatus) {
        return doPost(httpClient, url, paramsMap, null, null, null, null, checkResponseStatus);
    }

    public static String doPost(CloseableHttpClient httpClient, String url, Header[] headers, Map<String, String> paramsMap) {
        return doPost(httpClient, url, paramsMap, headers, null, null, null);
    }

    public static String doPost(CloseableHttpClient httpClient, String url, Header[] headers, Map<String, String> paramsMap, boolean checkResponseStatus) {
        return doPost(httpClient, url, paramsMap, headers, null, null, null, checkResponseStatus);
    }

    public static String doPostWithJson(CloseableHttpClient httpClient, String url, String jsonParam) {
        return doPostWithJson(httpClient, url, jsonParam, null);
    }

    public static String doPostWithJson(CloseableHttpClient httpClient, String url, String jsonParam, boolean checkResponseStatus) {
        return doPostWithJson(httpClient, url, jsonParam, null, checkResponseStatus);
    }

    public static String doPostWithJson(CloseableHttpClient httpClient, String url, Header[] headers, String jsonParam) {
        return doPostWithJson(httpClient, url, jsonParam, headers);
    }

    public static String doPostWithJson(CloseableHttpClient httpClient, String url, Header[] headers, String jsonParam, boolean checkResponseStatus) {
        return doPostWithJson(httpClient, url, jsonParam, headers, checkResponseStatus);
    }

    public static String doPostMultipartFrom(CloseableHttpClient httpClient, String url, Map<String, Object> paramsMap) {
        return doPostMultipartFrom(httpClient, null, url, paramsMap, null, null, null, null);
    }

    public static String doPostMultipartFrom(CloseableHttpClient httpClient, Charset charset, String url, Map<String, Object> paramsMap) {
        return doPostMultipartFrom(httpClient, charset, url, paramsMap, null, null, null, null);
    }

    public static String doPostMultipartFrom(CloseableHttpClient httpClient, String url, Map<String, Object> paramsMap, boolean checkResponseStatus) {
        return doPostMultipartFrom(httpClient, null, url, paramsMap, null, null, null, null, checkResponseStatus);
    }

    public static String doPostMultipartFrom(CloseableHttpClient httpClient, Charset charset, String url, Map<String, Object> paramsMap, boolean checkResponseStatus) {
        return doPostMultipartFrom(httpClient, charset, url, paramsMap, null, null, null, null, checkResponseStatus);
    }

    public static String doPostMultipartFrom(CloseableHttpClient httpClient, Charset charset, String url, Header[] headers, Map<String, Object> paramsMap) {
        return doPostMultipartFrom(httpClient, charset, url, paramsMap, headers, null, null, null);
    }

    public static String doPostMultipartFrom(CloseableHttpClient httpClient, Charset charset, String url, Header[] headers, Map<String, Object> paramsMap, boolean checkResponseStatus) {
        return doPostMultipartFrom(httpClient, charset, url, paramsMap, headers, null, null, null, checkResponseStatus);
    }

    /**
     * get请求
     */
    public static String doGetWithRequestParams(CloseableHttpClient client, String url, Header[] headers, boolean checkResponseStatus) {
        HttpRequestBase httpGet = new HttpGet(url);
        httpGet.setProtocolVersion(HttpVersion.HTTP_1_1);
        //自定义请求头Header
        if (headers != null && headers.length > 0) {
            httpGet.setHeaders(headers);
        }
        return doRequest(client, httpGet, null, checkResponseStatus);
    }

    public static String doGetWithRequestParams(CloseableHttpClient client, String url, Header[] headers) {
        return doGetWithRequestParams(client, url, headers, true);
    }

    /**
     * Map转UrlParam (不带'?')
     */
    public static String makeUrlParams(Map<String, String> map) {
        if (map == null || map.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * post请求 UrlEncodedForm
     */
    public static String doPost(CloseableHttpClient httpClient, String url, Map<String, String> paramsMap, Header[] headers,
                                Integer connTimeout, Integer reqTimeout, Integer socketTimeout, boolean checkResponseStatus) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
        if (connTimeout != null && reqTimeout != null && socketTimeout != null) {
            RequestConfig.Builder custom = RequestConfig.custom()
                    .setConnectTimeout(connTimeout)
                    .setConnectionRequestTimeout(reqTimeout)
                    .setSocketTimeout(socketTimeout);
            httpPost.setConfig(custom.build());
        }
        //自定义请求头Header
        if (headers != null && headers.length > 0) {
            httpPost.setHeaders(headers);
        }
        if (paramsMap != null && !paramsMap.isEmpty()) {
            List<NameValuePair> params = new ArrayList<>(paramsMap.size());
            paramsMap.forEach((key, value) -> params.add(new BasicNameValuePair(key, value)));
            httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
        }
        return doRequest(httpClient, httpPost, paramsMap, checkResponseStatus);
    }

    public static String doPost(CloseableHttpClient httpClient, String url, Map<String, String> paramsMap, Header[] headers,
                                Integer connTimeout, Integer reqTimeout, Integer socketTimeout) {
        return doPost(httpClient, url, paramsMap, headers, connTimeout, reqTimeout, socketTimeout, true);
    }

    /**
     * doPostMultipartFrom
     *
     * @param paramsMap String、File、InputStream
     */
    public static String doPostMultipartFrom(CloseableHttpClient httpClient, Charset charset, String url, Map<String, Object> paramsMap, Header[] headers,
                                             Integer connTimeout, Integer reqTimeout, Integer socketTimeout, boolean checkResponseStatus) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
        if (connTimeout != null && reqTimeout != null && socketTimeout != null) {
            RequestConfig.Builder custom = RequestConfig.custom()
                    .setConnectTimeout(connTimeout)
                    .setConnectionRequestTimeout(reqTimeout)
                    .setSocketTimeout(socketTimeout);
            httpPost.setConfig(custom.build());
        }
        //自定义请求头Header
        if (headers != null && headers.length > 0) {
            httpPost.setHeaders(headers);
        }
        if (paramsMap != null && !paramsMap.isEmpty()) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setCharset(charset != null ? charset : Charset.forName("UTF-8"));

            paramsMap.entrySet().stream().filter(e -> e.getValue() != null).forEach(e -> {
                if (e.getValue() instanceof InputStream) {
                    InputStreamBody inBody = new InputStreamBody((InputStream) e.getValue(), ContentType.MULTIPART_FORM_DATA, e.getKey());
                    multipartEntityBuilder.addPart(e.getKey(), inBody);
                    return;
                }
                if (e.getValue() instanceof File) {
                    FileBody fileBody = new FileBody((File) e.getValue());
                    multipartEntityBuilder.addPart(e.getKey(), fileBody);
                } else {
                    multipartEntityBuilder.addTextBody(e.getKey(), e.getValue().toString(), ContentType.create("text/plain", charset != null ? charset : Charset.forName("UTF-8")));
                }
            });

            httpPost.setEntity(multipartEntityBuilder.build());
        }

        Map<String, Object> logParamsMap = paramsMap == null ? null : paramsMap.entrySet().stream().filter(e -> !ignoreForLogParam(e.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return doRequest(httpClient, httpPost, logParamsMap, checkResponseStatus);
    }

    public static String doPostMultipartFrom(CloseableHttpClient httpClient, Charset charset, String url, Map<String, Object> paramsMap, Header[] headers,
                                             Integer connTimeout, Integer reqTimeout, Integer socketTimeout) {
        return doPostMultipartFrom(httpClient, charset, url, paramsMap, headers, connTimeout, reqTimeout, socketTimeout, true);
    }

    /**
     * 不记录日志的参数类型
     */
    private static boolean ignoreForLogParam(Object param) {
        return param instanceof InputStream;
    }

    /**
     * post请求(Json格式)
     */
    public static String doPostWithJson(CloseableHttpClient client, String url, String jsonParam, Header[] headers) {
        return doPostWithJson(client, url, jsonParam, headers, true);
    }

    public static String doPostWithJson(CloseableHttpClient client, String url, String jsonParam, Header[] headers, boolean checkResponseStatus) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
        //自定义请求头Header
        if (headers != null && headers.length > 0) {
            httpPost.setHeaders(headers);
        }
        //Json格式
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        // 设置请求参数
        StringEntity entity = new StringEntity(jsonParam, Charset.forName("UTF-8"));
        httpPost.setEntity(entity);
        return doRequest(client, httpPost, jsonParam, checkResponseStatus);
    }

    /**
     * doRequest
     */
    private static String doRequest(CloseableHttpClient httpClient, HttpRequestBase httpRequest, Object logRequestParam, boolean checkResponseStatus) {
        CloseableHttpResponse response = null;
        String method = httpRequest.getMethod();
        String url = httpRequest.getURI().toString();
        try {
            long start = System.currentTimeMillis();
            response = httpClient.execute(httpRequest);
            String responseStr = EntityUtils.toString(response.getEntity());

            if (log.isDebugEnabled()) {
                log.debug("请求Http 耗时{}ms method={} url={}, param={}, result={}", System.currentTimeMillis() - start, method, url, JsonUtils.toJson(logRequestParam), responseStr);
            }
            //httpCode为200，需要在调用方区分正常返回和业务异常返回
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode() && checkResponseStatus) {
                int httpCode = response.getStatusLine().getStatusCode();
                String httpMsg = response.getStatusLine().getReasonPhrase();
                throw new SystemException(String.valueOf(httpCode), httpMsg);
            }
            return responseStr;
        } catch (IOException e) {
            throw new SystemException("HTTP_REQUEST_ERROR", "Http请求IOException", e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.warn("关闭Http出错 method={} url={}, param={}", method, url, JsonUtils.toJson(logRequestParam), e);
                } finally {
                    response = null;
                }
            }
        }
    }
}
