/**
 * 
 */
package com.giveu.util;

import com.giveu.common.ConstantClsField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

/**
 * @author 529017
 *
 */
public class DESHelper {

	private static Logger logger = LoggerFactory.getLogger(DESHelper.class);
	/**
	 * 加密
	 * 
	 * @param data
	 *            数据源
	 * @param 密钥，长度16
	 * @return 返回加密后的数据
	 */
	public static String encryptData(String data, String password) {
		try {
			String iv = password.substring(0, 8);
			String pwd = password.substring(8);
			return byte2hex(DES_CBC_Encrypt(data.getBytes(), pwd.getBytes(), iv.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param 密钥，长度16
	 * @return 返回加密后的数据
	 */
	public static String decryptData(String data, String password) {
		try {
			String iv = password.substring(0, 8);
			String pwd = password.substring(8);
			return new String(DES_CBC_Decrypt(hex2byte(data), pwd.getBytes(), iv.getBytes()), ConstantClsField.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将二进制转化为16进制字符串
	 * 
	 * @param b
	 *            二进制字节数组
	 * @return String
	 */
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * 十六进制字符串转化为2进制
	 * 
	 * @param hex
	 * @return
	 */
	private static byte[] hex2byte(String hex) {
		byte[] tmp = hex.getBytes();
		byte[] ret = new byte[tmp.length / 2];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 * 
	 * @param src0
	 *            byte
	 * @param src1
	 *            byte
	 * @return byte
	 */
	private static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 加密
	 * 
	 * @param datasource
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 */
	public static byte[] encrypt(byte[] datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象,ENCRYPT_MODE用于将 Cipher 初始化为加密模式的常量
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource); // 按单部分操作加密或解密数据，或者结束一个多部分操作
		} catch (Throwable e) {
			logger.error("加密异常："+e.getMessage()+e.getStackTrace());
			//e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String password) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 返回实现指定转换的
																			// Cipher
																			// 对象
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}

	public static byte[] DES_CBC_Encrypt(byte[] content, byte[] keyBytes, byte[] ivBytes) {
		try {
			DESKeySpec keySpec = new DESKeySpec(keyBytes);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// cipher.init(Cipher.ENCRYPT_MODE, key, new
			// IvParameterSpec(keySpec.getKey()));
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			logger.error("加密异常："+e.getMessage()+e.getStackTrace());
			// TODO Auto-generated catch block
		}
		return null;
	}

	public static byte[] DES_CBC_Decrypt(byte[] content, byte[] keyBytes, byte[] ivBytes) {
		try {
			DESKeySpec keySpec = new DESKeySpec(keyBytes);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			logger.error("解密异常："+e.getMessage()+e.getStackTrace());
			// TODO Auto-generated catch block
		}
		return null;
	}
}