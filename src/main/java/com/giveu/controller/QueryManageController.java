package com.giveu.controller;

import com.giveu.common.ResponseCode;
import com.giveu.component.CheckSign;
import com.giveu.entity.EncryptData;
import com.giveu.entity.ReqQueryRefData;
import com.giveu.entity.ResponseModel;
import com.giveu.service.QueryPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/Api/queryPassword")
@CrossOrigin
public class QueryManageController {

    private static Logger logger = LoggerFactory.getLogger(QueryManageController.class);

    @Autowired
    QueryPasswordService queryPasswordServiceImpl;
    @Autowired
    CheckSign checkSign;

    @RequestMapping(value = "/encrypt")
    public ResponseModel encrypt(ReqQueryRefData data, HttpServletRequest request) {
        //ResponseModel<EncryptData> responseModel=new ResponseModel();
//        if (!checkSign.check(request)) {
//            responseModel.setMessage(ResponseCode.ERROR.getName());
//            responseModel.setCode(ResponseCode.ERROR.getIndex());
//            responseModel.setResult(false);
//            return responseModel;
//        }
        return queryPasswordServiceImpl.encrypt(data.getData(),data.getQueryPassword());
    }

    @RequestMapping(value = "/decrypt")
    public ResponseModel<EncryptData> decrypt(ReqQueryRefData data, HttpServletRequest request) {
//        ResponseModel<EncryptData> responseModel=new ResponseModel();
//        if (!checkSign.check(request)) {
//            responseModel.setMessage(ResponseCode.ERROR.getName());
//            responseModel.setCode(ResponseCode.ERROR.getIndex());
//            responseModel.setResult(false);
//            return responseModel;
//        }

        return queryPasswordServiceImpl.decrypt(data.getData(),data.getQueryPassword());
    }
}
