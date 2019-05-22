package com.stylefeng.guns.common.Excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class ExcelUtil {
    /**
     * 导出Excel
     *
     * @param myExcel 需要输出的excel
     * @param response  http响应
     * @return
     */
    public static void createExcel (MyExcel myExcel,HttpServletResponse response) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        if (myExcel == null) {
            return ;
        }
        if(myExcel.getExcelName().endsWith(".xls")) {
            myExcel.setExcelName(myExcel.getExcelName().replace(".xls", ".xlsx"));
        }
        for (int index = 0; index < myExcel.getSheets().size(); index++) {
            // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
            ExcelSheet mySheet = myExcel.getSheet(index);
            XSSFSheet sheet = wb.createSheet(mySheet.getSheetName());

            // 第四步，创建单元格，并设置值表头 设置表头居中
            //HSSFCellStyle style = wb.createCellStyle();
            //XSSFCellStyle style = wb.createCellStyle();
            //style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            XSSFRow row = sheet.createRow(0);
            //创建标题
            Object[] title = mySheet.getTitle();
            for (int i = 0; i < title.length; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(title[i].toString());
                //cell.setCellStyle(style);
            }

            Object[][] content = mySheet.getContent();
            for (int i = 0; i < content.length; i++) {
                // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
                XSSFRow rowCont = sheet.createRow(i + 1);
                for (int j = 0; j < content[i].length; j++) {
                    //将内容按顺序赋给对应的列对象
                    XSSFCell cell = rowCont.createCell(j);
                    cell.setCellValue(content[i][j].toString());
                    //cell.setCellStyle(style);
                    //rowCont.createCell(j).setCellValue(content[i][j].toString());
                }
            }
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel;charset=UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(myExcel.getExcelName(), "UTF-8"));
            wb.write(out);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static MyExcel readExcel(String filePath) {
        MyExcel myExcel = new MyExcel();
        ArrayList<ExcelSheet> sheets = new ArrayList<ExcelSheet>();

        File file = new File(filePath); //"D:/readExcel.xls"
        myExcel.setExcelName(file.getName());

        // 创建输入流，读取Excel
        FileInputStream in =null;
        try {
            org.apache.poi.ss.usermodel.Workbook wb = null;
            //String exName = filePath.substring(filePath.lastIndexOf("."), filePath.length());
            in = new FileInputStream(file);
            if (file.getName().endsWith("xls")) {     //Excel&nbsp;2003
                wb = new HSSFWorkbook(in);
            } else if (file.getName().endsWith("xlsx")) {    // Excel 2007/2010
                wb = new XSSFWorkbook(in);
            }

            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                String sheetName = wb.getSheetName(index);

                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(sheetName);
                ExcelSheet mySheet = new ExcelSheet();

                int rowCount = sheet.getPhysicalNumberOfRows(); // 返回该页的总行数

                if (rowCount == 0) {
                    continue;
                }
                Row row = sheet.getRow(0);
                int columnsCount = row.getPhysicalNumberOfCells(); // 返回该行的总列数
                Object[] title = new Object[columnsCount];
                Object[][] content = new Object[rowCount - 1][];
                for (int i = 0; i < rowCount; i++) {
                    row = sheet.getRow(i);
                    columnsCount = row.getPhysicalNumberOfCells(); // 返回该行的总列数
                    Object[] rowContent = new Object[columnsCount];
                    for (int j = 0; j < columnsCount; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            if (i == 0) {
                                title[j] = "";
                            } else {
                                content[i - 1][j] = "";
                            }
                            continue;
                        }
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        rowContent[j] = cell.getStringCellValue();
                    }
                    if (i == 0) {
                        title = rowContent;
                    } else {
                        content[i - 1] = rowContent;
                    }
                }
                mySheet.setTitle(title);
                mySheet.setSheetName(sheetName);
                mySheet.setContent(content);
                sheets.add(mySheet);
            }
            myExcel.setSheets(sheets);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if(in != null){
                    in.close();
                }
                if(file.exists()){
                    file.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return myExcel;
    }

    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }

    public String uploadFile(CommonsMultipartFile file, String uploadPath, String realUploadPath){
        InputStream is = null;
        OutputStream os = null;
        Calendar calendar = Calendar.getInstance();//获取时间
        long excelName = calendar.getTime().getTime();

        try {
            is = file.getInputStream();
            String des = realUploadPath + "/"+Long.toString(excelName)+file.getOriginalFilename();
            os = new FileOutputStream(des);

            byte[] buffer = new byte[1024];
            int len = 0;

            while((len = is.read(buffer))>0){
                os.write(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(is!=null){
                try{
                    is.close();
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }

            if(os!=null){
                try{
                    os.close();
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }
        }
        //返回路径
        return uploadPath + "/"+Long.toString(excelName)+file.getOriginalFilename();
    }

    /**
     * CSV文件生成方法
     * @param myExcel
     * @param path
     * @return
     */
    public static String createCSVFile(MyExcel myExcel, String path) {
        File csvFile = null;
        BufferedWriter csvWtriter = null;
        String filename="";
        try {
            filename=myExcel.getExcelName().replace(".xlsx",".csv").replace(".xls",".csv");
            csvFile = new File( path + File.separator + filename);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "UTF-8"), 1024);
            // 写入文件头部
            Object[] head=myExcel.getSheets().get(0).getTitle();
            writeRow(head, csvWtriter);

            // 写入文件内容
            Object[][] dataList=myExcel.getSheets().get(0).getContent();
            for (Object[] row : dataList) {
                writeRow(row, csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filename;
    }

    /**
     * 写一行数据方法
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(Object[] row, BufferedWriter csvWriter) throws IOException {
        // 写入文件头部
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data.toString()).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }
}
