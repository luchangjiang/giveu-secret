/**
 * 
 */
package com.giveu.common;

/**
 * @author 529017
 * 是否带星号枚举
 */
public enum MosaicType {	
	NO("不带星号",1),YES("带星号",2);
	
	// 成员变量  
    private String name;  
    private int index;  
    // 构造方法  
    private MosaicType(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(int index) {  
        for (MosaicType c : MosaicType.values()) {  
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
