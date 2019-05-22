package com.stylefeng.guns.modular.system.dao;

import com.giveu.entity.QueryManage;
import com.stylefeng.guns.common.persistence.model.QueryPwdManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 查询口令管理Dao
 *
 * @author fengshuonan
 * @Date 2018-09-04 20:20:09
 */
public interface QueryPwdManageDao {
    /**
     * 根据条件查询角色列表
     */
    List<Map<String, Object>> selectQueryPwd(@Param("condition") String condition);

    QueryManage getQuerPwdByPassword(@Param("password") String password);

    QueryManage getQuerPwdById(@Param("id") Integer id);

    Integer updateById(QueryManage var1);

    Integer insert(QueryManage var1);

    QueryManage selectById(Integer var1);

    Integer deleteById(Integer var1);
}
