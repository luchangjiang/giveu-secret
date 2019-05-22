package com.stylefeng.guns.modular.system.service;

import com.stylefeng.guns.common.Excel.MyExcel;
import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.common.util.SecretRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IQueryPageService {
    Tip sendRequest(String inputStr, String pwd, String type);

    List<String> postSecret(String url, SecretRequest request);

    MyExcel postSecret(String url, MyExcel request,String pwd);

    Tip handleFile(MultipartFile fileInfo,String inputType, String pwd, String filePath, HttpServletResponse response);
}
