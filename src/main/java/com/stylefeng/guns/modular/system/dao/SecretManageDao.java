package com.stylefeng.guns.modular.system.dao;

import com.giveu.entity.SecretManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 密口令Dao
 *
 * @author fengshuonan
 * @Date 2018-09-05 19:32:39
 */
public interface SecretManageDao {

    /**
     * 根据条件查询角色列表
     */
    List<Map<String, Object>> selectSecretManage(@Param("status") Integer status);

    List<SecretManage> getMaxSecretGroup(@Param("version") Integer version);

    List<SecretManage> getSecretGroup(@Param("secretType") int secretType);

    SecretManage getSecretManage(SecretManage secretManage);

    Integer updateSecretStatus(String uuid);

    Integer insertSecretManage(SecretManage secretManage);
}
