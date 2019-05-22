package com.stylefeng.guns.common.Excel;

public class ExcelSheet {
    private String sheetName;
    private Object[] title;
    private Object[][] content;


    public Object[][] getContent() {
        return content;
    }

    public void setContent(Object[][] content) {
        this.content = content;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Object[] getTitle() {
        return title;
    }

    public void setTitle(Object[] title) {
        this.title = title;
    }
}
