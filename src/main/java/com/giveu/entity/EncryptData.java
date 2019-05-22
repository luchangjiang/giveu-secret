package com.giveu.entity;

public class EncryptData {
    private String encrypt;
    private String refData;

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getRefData() {
        return refData;
    }

    public void setRefData(String refData) {
        this.refData = refData;
    }

    public EncryptData(String encrypt, String refData) {
        this.encrypt = encrypt;
        this.refData = refData;
    }
    public EncryptData() {}
}