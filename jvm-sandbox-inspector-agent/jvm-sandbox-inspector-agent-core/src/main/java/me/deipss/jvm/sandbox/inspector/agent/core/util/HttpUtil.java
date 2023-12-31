package me.deipss.jvm.sandbox.inspector.agent.core.util;

import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.HttpResult;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpUtil {
    public static final CloseableHttpClient client = HttpClients.custom().setConnectionTimeToLive(3, TimeUnit.MINUTES).build();

    public static HttpResult post(String url, String body, List<Header> headers) {
        HttpPost post = new HttpPost(url);
        HttpResult result = new HttpResult();
        if (null != headers && !headers.isEmpty()) {
            headers.forEach(post::addHeader);
        }
        post.addHeader("Content-Type", "application/json");
        try {
            HttpEntity httpEntity = new StringEntity(body, StandardCharsets.UTF_8);
            post.setEntity(httpEntity);
            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                result.setSuccess(true);
                result.setHttpStatus(code);
                result.setData(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
                return result;
            }
        } catch (IOException e) {
            log.error("http post异常，url={}", url, e);
        }
        result.setSuccess(false);
        return result;
    }

    public static HttpResult get(String url, Map<String, String> params, List<Header> headers) {

        HttpGet httpGet = new HttpGet(url);
        HttpResult result = new HttpResult();
        try {

            if (null != headers && !headers.isEmpty()) {
                headers.forEach(httpGet::addHeader);
            }
            if (null != params && !params.isEmpty()) {
                URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
                httpGet.setURI(uriBuilder.build());
            }
            HttpResponse response = client.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                result.setSuccess(true);
                result.setHttpStatus(code);
                result.setData(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
                return result;
            }
        } catch (IOException e) {
            log.error("http get error，url={},params={}", url, params, e);
        } catch (URISyntaxException e) {
            log.error("http get build uri error，url={},params={}", url, params, e);
        }
        result.setSuccess(false);
        return result;
    }
}
