package com.stylefeng.guns.common.util;

import org.apache.http.Header;

public class HttpResult {

    /**
     * 返回码，-1为失败
     */
    private int statusCode = -1;

    /**
     * 返回头信息
     */
    private Header[] headers;

    /**
     * 返回数据体
     */
    private byte[] body;

    public HttpResult(){}

    public HttpResult(int statusCode, Header[] headers, byte[] body){
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
