/**
 * 
 */
package com.giveu.common;

/**
 * @author 529017
 * 查询密钥类型枚举
 */
public enum SecretType {	
	ALL("所有",1),CURRENT("当前版本",2),FORBIDDEN("禁用",3);
	
	// 成员变量  
    private String name;  
    private int index;  
    // 构造方法  
    private SecretType(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(int index) {  
        for (SecretType c : SecretType.values()) {  
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
