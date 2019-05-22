package com.stylefeng.guns.modular.system.service;

import com.giveu.entity.SecretManage;
import com.stylefeng.guns.common.constant.tips.Tip;

import java.util.List;
import java.util.Map;

/**
 * 密口令Service
 *
 * @author fengshuonan
 * @Date 2018-09-05 19:32:39
 */
public interface ISecretManageService {

    List<Map<String, Object>> selectSecretManage(Integer status);

    List<SecretManage> getList(Integer secretType);
    Tip instead(String uuid);
    Tip insteadAll();
    Boolean createSecretGroup(String version);
}
