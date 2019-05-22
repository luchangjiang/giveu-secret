package com.giveu.dao.mysql;

import com.giveu.entity.QueryManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QueryManageDao {

    QueryManage getQueryManage(String queryCommand);

    List<QueryManage> getQueryManageList();

    Integer updateQueryUseTimes(@Param("useTimes") Integer useTimes, @Param("encryptTimes") Integer encryptTimes, @Param("decryptTimes") Integer decryptTimes,  @Param("pwd") String pwd);
}
