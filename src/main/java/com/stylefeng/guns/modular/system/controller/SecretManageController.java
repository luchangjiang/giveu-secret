package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.dao.SecretManageDao;
import com.stylefeng.guns.modular.system.service.ISecretManageService;
import com.stylefeng.guns.modular.system.warpper.SecretManageWarpper;
import org.apache.poi.hssf.record.BOFRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 密口令控制器
 *
 * @author fengshuonan
 * @Date 2018-09-05 19:32:39
 */
@Controller
@RequestMapping("/secretManage")
public class SecretManageController extends BaseController {

    private String PREFIX = "/system/secretManage/";

    /**
     * 跳转到密口令首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "secretManage.html";
    }

    @Autowired
    ISecretManageService secretManageServiceImpl;

    /**
     * 查询列表
     * @param status 查找类型
     * @return 返回密钥列表
     */
    @RequestMapping(value="/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) Integer status){

        List<Map<String, Object>> secretManage = this.secretManageServiceImpl.selectSecretManage(status);
        return super.warpObject(new SecretManageWarpper(secretManage));

//        ResponseModel<List<SecretManage>> responseModel=new ResponseModel<List<SecretManage>>();
//        try {
//            responseModel.setMessage(ResponseCode.OK.getName());
//            responseModel.setStatuCode(ResponseCode.OK.getIndex());
//            responseModel.setData(secretManageServiceImpl.getList(secretType));
//        } catch (Exception e) {
//            responseModel.setMessage(e.getMessage());
//            responseModel.setStatuCode(ResponseCode.ERROR.getIndex());
//        }
//        return responseModel;
    }

    /**
     * 禁用操作
     * @param uuid 密钥组唯一标志
     * @return
     */
    @RequestMapping(value="/instead")
    @ResponseBody
    public Tip instead(@RequestParam String uuid){
        if (ToolUtil.isEmpty(uuid)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        return secretManageServiceImpl.instead(uuid);
//        ResponseModel<String> responseModel=new ResponseModel<String>();
//        try {
//            responseModel.setMessage(ResponseCode.OK.getName());
//            responseModel.setStatuCode(ResponseCode.OK.getIndex());
//
//            secretManageServiceImpl.instead(uuid);
//
//            responseModel.setData("生成成功！");
//        } catch (Exception e) {
//            responseModel.setMessage(e.getMessage());
//            responseModel.setStatuCode(ResponseCode.ERROR.getIndex());
//        }
//        return responseModel;
    }

    /**
     * 36组密钥全部更新
     * @return
     */
    @RequestMapping(value="/insteadAll")
    @ResponseBody
    public Tip insteadAll(){
        return secretManageServiceImpl.insteadAll();

//        ResponseModel<String> responseModel=new ResponseModel<String>();
//        try {
//            responseModel.setMessage(ResponseCode.OK.getName());
//            responseModel.setStatuCode(ResponseCode.OK.getIndex());
//
//            secretManageServiceImpl.createSecretGroup("");
//
//            responseModel.setData("生成成功！");
//        } catch (Exception e) {
//            responseModel.setMessage(e.getMessage());
//            responseModel.setStatuCode(ResponseCode.ERROR.getIndex());
//        }
//        return responseModel;
    }
}
