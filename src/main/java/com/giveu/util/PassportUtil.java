/**
 * 
 */
package com.giveu.util;

import com.giveu.common.ConstantClsField;
import com.giveu.common.DataType;
import com.giveu.entity.SecretSecurity;
import com.giveu.util.PinyinTool.Type;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 529017
 * 加解密相关工具类
 */
public class PassportUtil {

	/**
	 * 根据输入原始数据获取密钥的模值
	 * @param source 待加密数据
	 * @return
	 */
	public static int getMod(String source) {
		try {
			DataType type = getDataType(source);

			String seed = getSource(type, source);

			int modValue = getModValue(seed);

			// System.out.println(modValue);
			return modValue;
		} catch (Exception e) {
			return ConstantClsField.FAIL_INT;
		}
	}

	/**
	 * 获取数据所属加解密类型
	 * @param source
	 * @return
	 */
	public static DataType getDataType(String source) {
		if(PassportUtil.isPhoneNum(source)){
			return DataType.PHONE;
		}
		if(PassportUtil.isPersonName(source)){
			return DataType.NAME;
		}
		if(PassportUtil.isIDCard(source)){
			return DataType.IDENT;
		}
		if(PassportUtil.isEmail(source)){
			return DataType.EMAIL;
		}
		if(PassportUtil.isBankNo(source)){
			return DataType.BANKCARD;
		}
		return DataType.OTHER;
	}

	/**
	 * 11位数号码，匹配格式：前三位固定格式+后8位任意数
	 * 此方法中前三位格式有：
	 * 13+任意数
	 * 15+除4的任意数
	 * 18+除1和4的任意数
	 * 17+除9的任意数
	 * 147
	 */
	public static boolean isPhoneNum(String phone) {
		String REGEX_ID_CARD = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		return Pattern.matches(REGEX_ID_CARD, phone);
	}
	/**
	 * 校验身份证
	 *
	 * @param idCard
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String idCard) {
		String REGEX_ID_CARD = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
		return Pattern.matches(REGEX_ID_CARD, idCard);
	}

	/**
	 * 判断时候为人名
	 * @param str
	 * @return
	 */
	public static boolean isPersonName(String str) {
		String REGEX_ID_CARD = "^[\\u4e00-\\u9fa5]+(·[\\u4e00-\\u9fa5]+)*$";
		return Pattern.matches(REGEX_ID_CARD, str);
	}

	/**
	 * 判断是否为银行卡
	 * @param str
	 * @return
	 */
	public static boolean isBankNo(String str) {
		if (str.length() < 12 || str.length() > 30) {
			return false;
		}
		String REGEX_ID_CARD = "[0-9]*";
		if (!Pattern.matches(REGEX_ID_CARD, str)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断时候为邮箱
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		String REGEX_ID_CARD = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
		return Pattern.matches(REGEX_ID_CARD, str);
	}

	/**
	 * 获取用于取模值的种子
	 * @param type 加解密类型
	 * @param source
	 * @return
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String getSource(DataType type, String source) throws BadHanyuPinyinOutputFormatCombination {
		String tempValue = "";

		if (source != null) {
			// 空白处理
			tempValue = source.replaceAll("\\s*|\t|\r|\n", "");
			// 中文转字母
			PinyinTool tool = new PinyinTool();
			tempValue = tool.toPinYin(tempValue, "", Type.LOWERCASE);
			// 全角到半角
			tempValue = BCConvert.qj2bj(tempValue);
			// 统一转换为小写
			tempValue = tempValue.toLowerCase();
		}

		switch (type) {
		case IDENT:// ident
		case PHONE:// phone
		case NAME:// name
		case OTHER:// other
		case BANKCARD:
			// System.out.println("中文值："+source);
			if (source.length() >= 2) {
				tempValue = tempValue.substring(tempValue.length() - 2);
			}
			break;
		case EMAIL:// email
			if (source.length() >= 2) {
				tempValue = tempValue.substring(0, 2);
			}
			break;
		default:
			tempValue = "";
			break;
		}
		return tempValue;
	}

	/**
	 * 获取模值 目前采取的均是前后两位字符作为取模
	 * @param source
	 * @return
	 */
	public static int getModValue(String source) {
		int modValue;
		if (source != null && source.length() > 0) {

			String firstChar = Integer.valueOf(source.charAt(0)).toString();
			// System.out.println("第一位值："+firstChar);

			String secondChar = "";
			if (source.length() > 1) {
				secondChar = Integer.valueOf(source.charAt(1)).toString();
				// System.out.println("第二位值："+secondChar);
			}

			int tempResult = Integer.parseInt(firstChar + secondChar);
			// System.out.println("转换后值："+tempResult);
			modValue = tempResult % 36;
			// System.out.println("取模值："+modValue);
		} else {
			modValue = ConstantClsField.FAIL_INT;
		}
		return modValue;
	}

	/**
	 * 全局安全码列表
	 * @return
	 */
	public static List<SecretSecurity> getSecurityCode() {
		List<SecretSecurity> list = new ArrayList<>();

		SecretSecurity secretSecurity = new SecretSecurity();
		secretSecurity.setVersion(1);
		secretSecurity.setSafeCode("SAFE*&^!");

		list.add(secretSecurity);

		return list;
	}

	/**
	 * 获取最大安全码
	 * @param list
	 * @return
	 */
	public static SecretSecurity getMaxSecurityCode(List<SecretSecurity> list) {
		SecretSecurity secretSecurity = new SecretSecurity();

		int tempValue = 0;
		int tempVersion = 0;
		for (SecretSecurity security : list) {
			tempVersion = security.getVersion();
			if (tempValue < tempVersion) {
				secretSecurity = security;
			}
		}

		return secretSecurity;
	}

	public static Integer getMaxVersion(){
		List<SecretSecurity> list =getSecurityCode();
		SecretSecurity secretSecurity =getMaxSecurityCode(list);
		return secretSecurity.getVersion();
	}

	
	/**
	 * 根据按键可用字符生成8位的字符串
	 * 密口令没有特殊字符，秘钥可以有特殊字符
	 * @param isContainSpecialChar 是否包含特殊字符
	 * @return
	 */
	public static String getRandomValue(Boolean isContainSpecialChar) {
		String[] chars1 = new String[]{"!", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/",
				":", ";", "<", "=", ">", "?", "@", "[", "]", "^", "_", "`", "{", "|", "}", "~", "a", "b", "c",
				"d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
				"x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
				"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		String[] chars2 = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
				"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
				"T", "U", "V", "W", "X", "Y", "Z"};

		String[] chars;
		if (isContainSpecialChar) {
			chars = chars1;
		} else {
			chars = chars2;
		}

		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}
}
