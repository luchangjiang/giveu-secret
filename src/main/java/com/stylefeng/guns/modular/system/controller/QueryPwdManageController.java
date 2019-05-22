package com.stylefeng.guns.modular.system.controller;

import com.giveu.entity.QueryManage;
import com.stylefeng.guns.common.util.ValidationUtil;
import com.stylefeng.guns.common.annotion.log.BussinessLog;
import com.stylefeng.guns.common.constant.Dict;
import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.common.persistence.model.QueryPwdManage;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.factory.QueryPwdManageFactory;
import com.stylefeng.guns.modular.system.service.IQueryPwdManageService;
import com.stylefeng.guns.modular.system.transfer.QueryPwdManageDto;
import com.stylefeng.guns.modular.system.warpper.QueryManageWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 查询口令管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-04 20:20:09
 */
@Controller
@RequestMapping("/queryPwdManage")
public class QueryPwdManageController extends BaseController {

    private String PREFIX = "/system/queryPwdManage/";

    @Autowired
    IQueryPwdManageService queryPwdManageServiceImpl;

    /**
     * 跳转到查询口令管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "queryPwdManage.html";
    }

    /**
     * 跳转到添加查询口令管理
     */
    @RequestMapping("/queryPwdManage_add")
    public String queryPwdManageAdd() {
        return PREFIX + "queryPwdManage_add.html";
    }

    /**
     * 跳转到修改查询口令管理
     */
    @RequestMapping("/queryPwdManage_update/{queryPwdManageId}")
    public String queryPwdManageUpdate(@PathVariable Integer queryPwdManageId, Model model) {
        if (ToolUtil.isEmpty(queryPwdManageId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        QueryManage queryPwd = this.queryPwdManageServiceImpl.getQueryPwdManageByid(queryPwdManageId);
        model.addAttribute(queryPwd);
        LogObjectHolder.me().set(queryPwd);

        return PREFIX + "queryPwdManage_edit.html";
    }

    /**
     * 获取查询口令管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String queryPwd) {
        List<Map<String, Object>> queryPwds = this.queryPwdManageServiceImpl.getQueryPwdList(queryPwd);
        return super.warpObject(new QueryManageWarpper(queryPwds));
    }

    /**
     * 新增查询口令管理
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "添加查询口令", key = "queryCommand", dict = Dict.QueryPwdManage)
    @ResponseBody
    public Tip add(@Valid QueryPwdManageDto queryPwdManage, BindingResult result) {
//        if (result.hasErrors()) {
//            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
//        }
        Tip tip = ValidationUtil.ValidError(queryPwdManage);
        if (tip.getCode() != 200) {
            return tip;
        }
        // 完善账号信息
        queryPwdManage.setCreateTime(new Date());
        queryPwdManage.setUpdateTime(new Date());
        queryPwdManage.setDecryptTimes(0);
        queryPwdManage.setUseTimes(0);
        queryPwdManage.setEncryptTimes(0);
        return queryPwdManageServiceImpl.insertQueryPwdManage(QueryPwdManageFactory.createQueryPwdManage(queryPwdManage));
    }

    /**
     * 删除查询口令管理(逻辑删除--禁用)
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer queryPwdManageId) {
        if (ToolUtil.isEmpty(queryPwdManageId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        return queryPwdManageServiceImpl.deleteQueryPwdManage(queryPwdManageId);
    }


    /**
     * 修改查询口令管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Tip update(@Valid QueryPwdManageDto queryPwdManage, BindingResult result) {
//        if (result.hasErrors()) {
//            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
//        }
        Tip tip = ValidationUtil.ValidError(queryPwdManage);
        if (tip.getCode() != 200) {
            return tip;
        }
        return queryPwdManageServiceImpl.updateQueryPwdById(QueryPwdManageFactory.createQueryPwdManage(queryPwdManage));
    }
}
