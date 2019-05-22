package com.stylefeng.guns.modular.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.giveu.entity.EncryptData;
import com.giveu.entity.ResponseModel;
import com.giveu.service.QueryPasswordService;
import com.stylefeng.guns.common.Excel.ExcelSheet;
import com.stylefeng.guns.common.Excel.ExcelUtil;
import com.stylefeng.guns.common.Excel.MyExcel;
import com.stylefeng.guns.common.constant.tips.ErrorTip;
import com.stylefeng.guns.common.constant.tips.SuccessTip;
import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.common.util.SecretRequest;
import com.stylefeng.guns.modular.system.service.IQueryPageService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QueryPageServiceImpl implements IQueryPageService {

    private final static Logger log = LoggerFactory.getLogger(QueryPageServiceImpl.class);

    @Autowired
    QueryPasswordService queryPasswordService;

    @Override
    public Tip sendRequest(String inputStr, String pwd, String type) {
        if (inputStr == null || inputStr.isEmpty() || inputStr.trim().equals("")) {
            ErrorTip tip = new ErrorTip(0, "输入内容不能为空");
            return tip;
        }
        if (pwd == null || pwd.isEmpty() || pwd.trim().equals("")) {
            ErrorTip tip = new ErrorTip(0, "查询口令不能为空");
            return tip;
        }
        SecretRequest request = new SecretRequest();
        String[] list = inputStr.split("\n");
        request.setData(Arrays.asList(list));
        request.setQueryPassword(pwd);

        String strResult = "";
        ResponseModel<EncryptData> res;
        if (type.contains("encrypt")) {
            res = queryPasswordService.encrypt(request.getData(), request.getQueryPassword());
            if (res == null || res.getData() == null || res.getResult() == false) {
                ErrorTip tip = new ErrorTip(0, res.getMessage());
                return tip;
            } else {
                for (int i = 0; i < res.getData().size(); i++) {
                    strResult += res.getData().get(i).getEncrypt() + "\n";
                }
            }
        } else {
            res = queryPasswordService.decrypt(request.getData(), request.getQueryPassword());
            if (res == null || res.getData() == null || res.getResult() == false) {
                ErrorTip tip = new ErrorTip(0, res.getMessage());
                return tip;
            } else {
                for (int i = 0; i < res.getData().size(); i++) {
                    strResult += res.getData().get(i).getRefData() + "\n";
                }
            }
        }
        //List<String> res=queryPageServiceImpl.postSecret(type,request);
        SuccessTip tip = new SuccessTip();
        tip.setMessage(strResult);
        return tip;
    }

    @Override
    public List<String> postSecret(String type, SecretRequest request) {
        JSONObject json = JSON.parseObject(JSON.toJSONString(request));
        log.info("postSecret-Send:" + json);
        try {

            ArrayList<String> list = new ArrayList<>();
            ResponseModel<EncryptData> res;
            if (type.contains("encrypt")) {
                res = queryPasswordService.encrypt(request.getData(), request.getQueryPassword());
                if (res != null && res.getData() != null) {
                    for (int i = 0; i < res.getData().size(); i++) {
                        list.add(res.getData().get(i).getEncrypt());
                    }
                }
            } else {
                res = queryPasswordService.decrypt(request.getData(), request.getQueryPassword());
                if (res != null && res.getData() != null) {
                    for (int i = 0; i < res.getData().size(); i++) {
                        list.add(res.getData().get(i).getRefData());
                    }
                }
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage() + e.getStackTrace());
        }
        return null;
    }

    @Override
    public MyExcel postSecret(String type, MyExcel excle, String pwd) {
        SecretRequest request = new SecretRequest();
        request.setQueryPassword(pwd);
        request.setMosaic(0);
        MyExcel resultExcel=new MyExcel();
        resultExcel.setExcelName(excle.getExcelName());
        ArrayList<ExcelSheet> sheetList=new ArrayList<>();

        for (ExcelSheet sheet : excle.getSheets()) {
            ExcelSheet resultSheet=new ExcelSheet();
            resultSheet.setTitle(sheet.getTitle());
            resultSheet.setSheetName(sheet.getSheetName());

            List<String> list = new ArrayList<>();
            Object[][] content = sheet.getContent();
            for (int i = 0; i < content.length; i++) {
                for (int j = 0; j < content[i].length; j++) {
                    list.add(content[i][j].toString());
                }
            }
            request.setData(list);
            List<String> resultList = postSecret(type, request);
            int count=0;
            for (int i = 0; i < content.length; i++) {
                for (int j = 0; j < content[i].length; j++) {
                    content[i][j] = resultList.get(count);
                    count++;
                }
            }
            resultSheet.setContent(content);
            sheetList.add(resultSheet);
        }
        resultExcel.setSheets(sheetList);
        return resultExcel;
    }

    @Override
    public Tip handleFile(MultipartFile fileInfo, String inputType, String pwd, String filePath, HttpServletResponse response) {
        if (inputType == null || inputType.isEmpty() || inputType.trim().equals("")) {
            ErrorTip tip = new ErrorTip(0, "未知操作");
            return tip;
        }
        if (!inputType.equals("encryptFile") && !inputType.equals("decryptFile")) {
            ErrorTip tip = new ErrorTip(0, "未知操作");
            return tip;
        }
        if (pwd == null || pwd.isEmpty() || pwd.trim().equals("")) {
            ErrorTip tip = new ErrorTip(0, "查询口令不能为空");
            return tip;
        }
        if (fileInfo == null) {
            ErrorTip tip = new ErrorTip(0, "上传文件不能为空");
            return tip;
        }
        Tip tip;
        FileOutputStream outStream = null;
        InputStream input = null;
        try {
            String uploadFileName = fileInfo.getOriginalFilename();
            //String filePath = request.getSession().getServletContext().getRealPath("/") + "handleExcelFile\\";//"/static/download/"; //
            long size = fileInfo.getSize();
            byte[] data = new byte[(int) size];
            input = fileInfo.getInputStream();
            input.read(data);
            File outFile = new File(filePath + uploadFileName);
            if (!outFile.isDirectory()) {
                outFile.mkdirs();
            }
            if (outFile.exists()) {
                outFile.delete();
            }

            File allFile = new File(filePath);
            File[] fileList = allFile.listFiles();//将该目录下的所有文件放置在一个File类型的数组中
            for (File itemFile: fileList) {
                if (itemFile.exists()) {
                    itemFile.delete();
                }
            }

            outFile.createNewFile();
            outStream = new FileOutputStream(outFile);
            outStream.write(data);
            outStream.flush();


            MyExcel myExcel = ExcelUtil.readExcel(filePath + uploadFileName);
            MyExcel excel = postSecret(inputType, myExcel, pwd);
            //String fName = ExcelUtil.createCSVFile(excel,filePath);
            ExcelUtil.createExcel(excel,response);

            tip = new SuccessTip();
            tip.setMessage("处理成功");

        } catch (Exception ex) {
            log.error(ex.getMessage() + ex.getStackTrace());
            tip = new ErrorTip(0, "操作异常"+ex.getMessage());
        }
        finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tip;
    }
}
