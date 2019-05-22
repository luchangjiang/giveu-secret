package com.giveu.util;

import jdk.internal.dynalink.beans.StaticClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static Logger logger = LoggerFactory.getLogger(AESUtil.class);

    /**
     * AES加密字符串
     *
     * @param content 需要被加密的字符串
     * @param password    加密需要的密码
     * @return 密文
     */
    public static String encrypt(String content, String password) {
        try {
            if (password == null) {
                logger.error("Key为空null");
                return null;
            }
            String ivs = password;
            String pwd = password;

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
            return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
        } catch (Exception e) {
            logger.error("加密失败" + e.getMessage() + e.getStackTrace());
        }
        return "";
    }

    public static String decrypt(String content, String password) {
        if (password == null) {
            logger.error("Key为空null");
            return null;
        }
        try {
            String ivs = password;
            String pwd = password;
            byte[] raw = pwd.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(content);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception e) {
            logger.error("解密失败" + e.getMessage() + e.getStackTrace());
        }
        return "";
    }
}
