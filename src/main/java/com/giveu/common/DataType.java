/**
 * 
 */
package com.giveu.common;

/**
 * @author 529017
 * 需要加解密的数据类型枚举
 */
public enum DataType {   
	//IDENT,EMAIL,PHONE,NAME,Other
	
	IDENT("身份证",1),EMAIL("邮箱",2),PHONE("电话",3),NAME("姓名",4),BANKCARD("银行卡",5),OTHER("其他",999);
	
	// 成员变量  
    private String name;  
    private int index;  
    // 构造方法  
    private DataType(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(int index) {  
        for (DataType c : DataType.values()) {  
            if (c.getIndex() == index) {  
                return c.name;  
            }  
        }  
        return null;  
    }  
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getIndex() {  
        return index;  
    }  
    public void setIndex(int index) {  
        this.index = index;  
    }
}
