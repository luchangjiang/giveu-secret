/**
 * 
 */
package com.giveu.common;

/**
 * @author 529017
 * 返回码类型枚举
 */
public enum ResponseCode {
	OK("OK","200"),	ERROR("ERROR","500"),	FORBIDDEN("无权限","403");
	   // 成员变量  
    private String name;  
    private String index;  
    // 构造方法  
    private ResponseCode(String name, String index) {  
        this.name = name;
        this.index = index;  
    }
    // 普通方法  
    public static String getName(String index) {  
        for (ResponseCode c : ResponseCode.values()) {  
            if (c.getIndex().equals(index)) {  
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
    public String getIndex() {  
        return index;  
    }  
    public void setIndex(String index) {  
        this.index = index;  
    }
}
