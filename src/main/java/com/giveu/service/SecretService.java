/**
 * 
 */
package com.giveu.service;

import com.giveu.entity.EncryptData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 529017
 * 加解密服务
 */
public interface SecretService {
	ArrayList<EncryptData> encrypt(List<String> sources);
	ArrayList<EncryptData> decrypt(List<String> sources);

	ArrayList<EncryptData> decrypt(List<String> sources, Integer mosaic);
}
