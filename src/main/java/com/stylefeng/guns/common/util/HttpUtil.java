package com.stylefeng.guns.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public class HttpUtil {

    public static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private static final int READ_TIMEOUT = 10000 * 30;

    private static final int CONNECT_TIMEOUT = 10000 * 10;

    public static HttpResult get(String url) throws IOException{
        return get(url, null, null);
    }

    public static HttpResult get(String url, Map<String,String> params) throws IOException{
        return get(url, params, null);
    }

    /**
     * get请求
     * @param url 请求url
     * @param params 请求参数
     * @param headers 请求http头信息
     * @return
     * @throws IOException
     */
    public static HttpResult get(String url, Map<String,String> params, Properties headers) throws IOException{
        HttpResult httpResult = new HttpResult();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //参数
        if(params != null){
            String paramStr = "";
            for (String key : params.keySet()) {
                paramStr += (key + "=" + params.get(key) + "&");
            }
            if (paramStr.lastIndexOf("&") > 0) {
                paramStr = paramStr.substring(0, paramStr.length() - 1);
            }
            url += ("?" + paramStr);
        }

        HttpGet get = new HttpGet(url);
        get.setConfig(getHttpConfig());

        //设置头信息
        if(headers != null){
            Enumeration<Object> keys = headers.keys();
            while(keys.hasMoreElements()){
                String key = (String) keys.nextElement();
                get.addHeader(key, headers.getProperty(key));
            }
        }

        HttpResponse response;
        byte[] data;
        try {
            response = httpClient.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            httpResult.setStatusCode(statusCode);
            httpResult.setHeaders(response.getAllHeaders());
            if(statusCode != HttpStatus.SC_OK) {
                log.error("reqeust error [GET]: url={}, code={}", url, statusCode);
            }
            data = EntityUtils.toByteArray(response.getEntity());
            httpResult.setBody(data);
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            log.error("reqeust error [GET]: url={}, messgae={}", url, e.getMessage(), e);
            throw e;
        } finally {
            if(get != null) {
                get.releaseConnection();
            }
            httpClient.close();
        }
        return httpResult;
    }

    public static HttpResult post(String url) throws IOException{
        return post(url, null, null);
    }

    public static HttpResult post(String url, Map<String,Object> params) throws IOException{
        return post(url, params, null);
    }

    public static HttpResult post(String url, JSONObject json) throws IOException {
        return post(url, json, null);
    }

    /**
     * post请求 json模式
     * @param url
     * @param json
     * @param headers
     * @return
     * @throws IOException
     */
    public static HttpResult post(String url, JSONObject json, Properties headers) throws IOException {
        HttpResult httpResult = new HttpResult();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setConfig(getHttpConfig());

        if(json != null) {
            StringEntity entity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
            entity.setContentEncoding("UTF-8");
            post.setEntity(entity);
        }

        //设置头信息
        if(headers != null){
            Enumeration<Object> keys = headers.keys();
            while(keys.hasMoreElements()){
                String key = (String) keys.nextElement();
                post.addHeader(key, headers.getProperty(key));
            }
        }

        HttpResponse response;
        byte[] data;
        try {
            response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            httpResult.setStatusCode(statusCode);
            httpResult.setHeaders(response.getAllHeaders());
            if(statusCode != HttpStatus.SC_OK) {
                log.error("reqeust error [POST]: url={}, code={}", url, statusCode);
            }
            data = EntityUtils.toByteArray(response.getEntity());
            httpResult.setBody(data);
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            log.error("reqeust error [POST]: url={}, messgae={}", url, e.getMessage(), e);
            throw e;
        } finally {
            if(post != null) {
                post.releaseConnection();
            }
            httpClient.close();
        }
        return httpResult;
    }

    /**
     * post请求 form模式
     * @param url 请求url
     * @param params 请求参数
     * @param headers 请求http头信息
     * @return
     * @throws IOException
     */
    public static HttpResult post(String url, Map<String,Object> params, Properties headers) throws IOException{
        HttpResult httpResult = new HttpResult();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setConfig(getHttpConfig());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //参数
        if(params != null){
            for(Map.Entry<String, Object> param : params.entrySet()){
                Object value = param.getValue();
                if(value instanceof String){
                    builder.addTextBody(param.getKey(), (String)value);
                }else if(value instanceof File){
                    builder.addPart(param.getKey(), new FileBody((File) value));
                }
            }
            post.setEntity(builder.build());
        }
        //设置头信息
        if(headers != null){
            Enumeration<Object> keys = headers.keys();
            while(keys.hasMoreElements()){
                String key = (String) keys.nextElement();
                post.addHeader(key, headers.getProperty(key));
            }
        }

        HttpResponse response;
        byte[] data;
        try {
            response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            httpResult.setStatusCode(statusCode);
            httpResult.setHeaders(response.getAllHeaders());
            if(statusCode != HttpStatus.SC_OK) {
                log.error("reqeust error [POST]: url={}, code={}", url, statusCode);
            }
            data = EntityUtils.toByteArray(response.getEntity());
            httpResult.setBody(data);
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            log.error("reqeust error [POST]: url={}, messgae={}", url, e.getMessage(), e);
            throw e;
        } finally {
            if(post != null) {
                post.releaseConnection();
            }
            httpClient.close();
        }
        return httpResult;
    }

    public static RequestConfig getHttpConfig(){
        return RequestConfig.custom().setConnectionRequestTimeout(1000)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(READ_TIMEOUT).build();
    }
}
