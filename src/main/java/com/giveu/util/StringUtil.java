package com.giveu.util;

/**
 * 字符串工具类
 * Created by fox on 2018/9/5.
 */
public class StringUtil {

	public static boolean isEmptyBatch(Object... objects){
		for (Object object : objects) {
			if(object == null)
				return true;
		}
		return false;
	}

	public static boolean isNotEmptyBatch(Object... objects){
		for (Object object : objects) {
			if(object == null)
				return false;
		}
		return true;
	}
}
