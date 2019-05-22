package com.stylefeng.guns.modular.system.service;

import com.giveu.entity.QueryManage;
import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.common.persistence.model.QueryPwdManage;

import java.util.List;
import java.util.Map;

/**
 * 查询口令管理Service
 *
 * @author fengshuonan
 * @Date 2018-09-04 20:20:09
 */
public interface IQueryPwdManageService {

    QueryManage getQueryPwdManageByid(Integer queryPwdManageId);

    List<Map<String, Object>> getQueryPwdList(String queryPwd);

    Tip updateQueryPwdById(QueryManage queryPwdManage);

    QueryManage getQuerPwdByPassword(String command);

    Tip insertQueryPwdManage(QueryManage queryPwdManage);

    Tip deleteQueryPwdManage(Integer queryPwdManageId);
}
