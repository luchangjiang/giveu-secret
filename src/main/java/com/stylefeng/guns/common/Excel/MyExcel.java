package com.stylefeng.guns.common.Excel;

import java.util.ArrayList;

public class MyExcel {

    private String excelName;
    private ArrayList<ExcelSheet> sheets;

    public ExcelSheet getSheet(int index){
        return sheets.get(index);
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public ArrayList<ExcelSheet> getSheets() {
        return sheets;
    }

    public void setSheets(ArrayList<ExcelSheet> sheets) {
        this.sheets = sheets;
    }
}
