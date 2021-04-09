package vip.itchen.support;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by lhb on 2019/2/26
 */
@Slf4j
public class HttpRequestUtil {
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 30 * 1000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(400);
        connMgr.setDefaultMaxPerRoute(100);
        connMgr.setValidateAfterInactivity(5 * 1000);

        requestConfig = RequestConfig.custom()
                // 设置连接超时
                .setConnectTimeout(MAX_TIMEOUT)
                // 设置读取超时
                .setSocketTimeout(MAX_TIMEOUT)
                // 设置从连接池获取连接实例的超时
                .setConnectionRequestTimeout(MAX_TIMEOUT)
                .build();
    }

    public static JSONObject post(String apiUrl, Map<String, String> requestHeader, JSONObject requestBody) {
        HttpPost httpPost = new HttpPost(apiUrl);
        // 构造消息头部
        requestHeader.forEach(httpPost::setHeader);
        // 构造消息内容
        httpPost.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        String returnContent = "";
        try {
            response = httpClient.execute(httpPost);
            returnContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.error("URL:{} Header:{} Body:{} 请求失败。", apiUrl, requestHeader, requestBody.toString(), e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSONObject.parseObject(returnContent, Feature.InitStringFieldAsEmpty);
    }

    public static JSONObject get(String apiUrl, Map<String, String> requestHeader, Map<String, String> paramMap) {
        HttpGet httpGet = new HttpGet(apiUrl);

        if (null != requestHeader && !requestHeader.isEmpty()) {
            // 构造消息头部
            requestHeader.forEach(httpGet::setHeader);
        }
        if (null != paramMap && !paramMap.isEmpty()) {
            List<NameValuePair> formParams = setHttpParams(paramMap);
            String param = URLEncodedUtils.format(formParams, "UTF-8");
            httpGet.setURI(URI.create(apiUrl + "?" + param));
        } else {
            httpGet.setURI(URI.create(apiUrl));
        }

        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        String returnContent = "";
        try {
            response = httpClient.execute(httpGet);
            returnContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.error("URL:{} Header:{} 请求失败。", apiUrl, requestHeader, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSONObject.parseObject(returnContent, Feature.InitStringFieldAsEmpty);
    }

    private static CloseableHttpClient getHttpClient() {
        HttpClientBuilder builder = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connMgr)
                .setRetryHandler((exception, executionCount, context) -> {
                    if (executionCount >= 3) {
                        // Do not retry if over max retry count
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {
                        // Timeout
                        return true;
                    }
                    if (exception instanceof UnknownHostException) {
                        // Unknown host
                        return true;
                    }
                    if (exception instanceof SSLException) {
                        // SSL handshake exception
                        return true;
                    }
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
                    return !(request instanceof HttpEntityEnclosingRequest);
                });
        if (useProxy()) {
            // IP代理 本地测试专用
            builder.setProxy(new HttpHost("127.0.0.1", 10809));
        }
        return builder.build();
    }

    private static List<NameValuePair> setHttpParams(Map<String, String> paramMap) {
        List<NameValuePair> formParams = new ArrayList<>();
        Set<Map.Entry<String, String>> set = paramMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formParams;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "https://api.rms.rakuten.co.jp/es/2.0/order/getOrder/";
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "ESA U1AzMTk3ODBfY0R5bVBPSHFYaTlPa2lYWjpTTDMxOTc4MF9ub0tiQkhwZWZyeVNDQXVD");
        header.put("Content-Type", "application/json; charset=utf-8");

        JSONObject postData = new JSONObject();
        postData.put("orderNumberList", Lists.newArrayList("319780-20190108-00744729"));
        postData.put("version", "2");

        log.info("Start1");
        JSONObject obj = HttpRequestUtil.post(url, header, postData);
        log.info(obj == null ? "None" : obj.toString());

        TimeUnit.SECONDS.sleep(30);

        log.info("Start2");
        obj = HttpRequestUtil.post(url, header, postData);
        log.info(obj == null ? "None" : obj.toString());

        TimeUnit.SECONDS.sleep(30);

        log.info("Start3");
        obj = HttpRequestUtil.post(url, header, postData);
        log.info(obj == null ? "None" : obj.toString());
    }

    private static boolean useProxy() {
        return false;
    }
}
