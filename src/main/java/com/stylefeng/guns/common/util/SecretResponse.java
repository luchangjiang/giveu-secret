package com.stylefeng.guns.common.util;

import java.util.ArrayList;

public class SecretResponse {
    private String message;
    private String statuCode;
    private ArrayList<String> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatuCode() {
        return statuCode;
    }

    public void setStatuCode(String statuCode) {
        this.statuCode = statuCode;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
