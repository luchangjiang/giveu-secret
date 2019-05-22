package com.stylefeng.guns.modular.system.factory;

import com.giveu.entity.QueryManage;
import com.stylefeng.guns.common.persistence.model.QueryPwdManage;
import com.stylefeng.guns.modular.system.transfer.QueryPwdManageDto;
import org.springframework.beans.BeanUtils;

public class QueryPwdManageFactory {

    public static QueryManage createQueryPwdManage(QueryPwdManageDto queryPwdManageDto){
        if(queryPwdManageDto == null){
            return null;
        }else{
            QueryManage queryPwdManage = new QueryManage();
            BeanUtils.copyProperties(queryPwdManageDto,queryPwdManage);
            return queryPwdManage;
        }
    }
}
