package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.modular.system.service.IQueryPageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 查询页面控制器
 *
 * @author 101320
 * @Date 2018-08-29 15:09:15
 */
@Controller
@RequestMapping("/queryPage")
public class QueryPageController extends BaseController {
    private final static Logger log = LoggerFactory.getLogger(QueryPageController.class);
    private String PREFIX = "/system/queryPage/";

    @Autowired
    IQueryPageService queryPageServiceImpl;

    /**
     * 跳转到查询文本页面首页
     */
    @RequestMapping("/text")
    public String text() {
        return PREFIX + "queryPageText.html";
    }

    /**
     * 跳转到查询文件页面首页
     */
    @RequestMapping("/file")
    public String file() {
        return PREFIX + "queryPageFile.html";
    }

    /**
     * 文本加密
     */
    @ApiOperation("文本加密")
    @RequestMapping(value = "/encryptText")
    @ResponseBody
    public Tip encryptText(String inputStr, String pwd) {
        return queryPageServiceImpl.sendRequest(inputStr, pwd, "encryptText");
    }

    /**
     * 文本解密
     */
    @RequestMapping("/decryptText")
    @ApiOperation("文本解密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inputStr", value = "输入文本", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pwd", value = "查询口令", required = true, dataType = "String")
    })
    @ResponseBody
    public Tip decryptText(String inputStr, String pwd) {
        return queryPageServiceImpl.sendRequest(inputStr, pwd, "decryptText");
    }

    @ApiOperation("文件解密")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inputType", value = "输入类型", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pwd", value = "查询口令", required = true, dataType = "String")
    })
    @RequestMapping(value = "handleFile", method = RequestMethod.POST)
    public void handleFile(@RequestParam(value = "fileinfo") MultipartFile fileinfo,
                             HttpServletRequest request, HttpServletResponse response, String inputType, String pwd) {

        String realPath = request.getSession().getServletContext().getRealPath("/") + "handleExcelFile/";
        queryPageServiceImpl.handleFile(fileinfo, inputType, pwd, realPath,response);
    }
}
