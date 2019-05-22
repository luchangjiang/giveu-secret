package com.giveu.service;

import com.giveu.entity.ResponseModel;

import java.util.List;

public interface QueryPasswordService {

    /**
     * 获取密口令可用次数
     * @param pwd  秘钥
     * @param type 类型 0：加密 1：解密
     * @return
     */
    ResponseModel<String> getAvailableTimes (String pwd,Integer type);

    ResponseModel decrypt(List<String> sources, String pwd);

    ResponseModel encrypt(List<String> sources, String pwd);
}
