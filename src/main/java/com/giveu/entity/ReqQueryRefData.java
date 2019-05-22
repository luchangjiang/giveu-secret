package com.giveu.entity;

import java.util.List;

public class ReqQueryRefData {
    private static final long serialVersionUID = 1L;

    private Integer mosaic;

    private List<String> data;

    private String queryPassword;

    public Integer getMosaic() {
        return mosaic;
    }

    public void setMosaic(Integer mosaic) {
        this.mosaic = mosaic;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getQueryPassword() {
        return queryPassword;
    }

    public void setQueryPassword(String queryPassword) {
        this.queryPassword = queryPassword;
    }
}
