/**
 * 
 */
package com.giveu.service.impl;

import com.giveu.common.MosaicType;
import com.giveu.common.ResponseCode;
import com.giveu.component.AsyncSecretHandle;
import com.giveu.component.RedisToDBService;
import com.giveu.entity.EncryptData;
import com.giveu.entity.SecretManage;
import com.giveu.entity.SecretSecurity;
import com.giveu.service.SecretService;
import com.giveu.util.DESHelper;
import com.giveu.util.PassportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 529017
 *
 */
@Service
public class SecretServiceImpl implements SecretService {

//	@Autowired
//	SecretDao secretDao;

	@Autowired
    RedisToDBService redisToDBService;

	@Autowired
	AsyncSecretHandle asyncSecretHandle;

	SecretServiceImpl() {

	}

	/**
	 * 
	 * 加密服务
	 * 
	 */
	@Override
	public ArrayList<EncryptData> encrypt(List<String> sources) {
        ArrayList<EncryptData> targets = new ArrayList<>();
        if(sources==null){
            return targets;
        }
        Map<String, Integer> commandMap = new HashMap(200);
        List<SecretSecurity> securityManagers = PassportUtil.getSecurityCode();
        SecretSecurity secretSecurity = PassportUtil.getMaxSecurityCode(securityManagers);
        //List<SecretManage> secretManages = secretDao.getMaxSecretGroup(secretSecurity.getVersion());
        List<SecretManage> list = redisToDBService.getPwdByCommand(null, 1, secretSecurity.getVersion(), null);
        Map<Integer, SecretManage> map = new HashMap<>(200);
        for (SecretManage s : list) {
            map.put(s.getSecretIndex(), s);
        }
        if (sources == null) {
            return targets;
        }
        for (String data : sources) {
            EncryptData encryptData = new EncryptData(data, data);
            if (data == null || data.trim().equals("")) {
                targets.add(encryptData);
                continue;
            }
            data = data.trim();
            int index = PassportUtil.getMod(data);
            SecretManage secretManage = map.get(index);
            if (encryptData != null && secretManage.getSecretPassword() != null && !secretManage.getSecretPassword().isEmpty()) {
                String password = secretSecurity.getSafeCode() + secretManage.getSecretPassword();
                String result = DESHelper.encryptData(data, password);
                //String result = AESUtil.encrypt(data, password);

                if (result != null && !result.trim().isEmpty()) {
                    encryptData.setEncrypt(secretManage.getSecretCommand() + result);
                }
                if (commandMap.containsKey(secretManage.getSecretCommand())) {
                    commandMap.put(secretManage.getSecretCommand(), commandMap.get(secretManage.getSecretCommand()) + 1);
                } else {
                    commandMap.put(secretManage.getSecretCommand(), 1);
                }
            }
            targets.add(encryptData);
        }
        asyncSecretHandle.recordEncrypt(commandMap);
        return targets;
    }

	/**
	 * 
	 * 解密服务
	 * 
	 */
	@Override
	public ArrayList<EncryptData> decrypt(List<String> sources) {
        ArrayList<EncryptData> targets = new ArrayList<>();
        if(sources==null){
            return targets;
        }
		Map<String, Integer> commandMap = new HashMap(200);
		List<SecretSecurity> securityList = PassportUtil.getSecurityCode();
		SecretSecurity secretSecurity = PassportUtil.getMaxSecurityCode(securityList);
		//List<SecretManage> secretManages = secretDao.getMaxSecretGroup(secretSecurity.getVersion());
		List<SecretManage> list = redisToDBService.getPwdByCommand(null, null, null,null);
		Map<String, SecretManage> map = new HashMap<>(200);
		for (SecretManage s : list) {
			map.put(s.getSecretCommand(), s);
		}
		for (String data : sources) {
            EncryptData encryptData = new EncryptData(data,data);
			if (data.length() < 8) {
				targets.add(encryptData);
				continue;
			}
			String command = data.substring(0, 8);
			SecretManage sm = map.get(command);
			if (sm == null) {
                targets.add(encryptData);
				continue;
			}
			String pwd = sm.getSecretPassword();
			if (pwd != null && !pwd.isEmpty()) {
				String password = secretSecurity.getSafeCode() + pwd;
				String result = DESHelper.decryptData(data.substring(8), password);
				//String result = AESUtil.decrypt(data, password);
				if (result != null && !result.trim().isEmpty()) {
                    encryptData.setRefData(result);
				}
				if (commandMap.containsKey(command)) {
					commandMap.put(command, commandMap.get(command) + 1);
				} else {
					commandMap.put(command, 1);
				}
			}
			targets.add(encryptData);
		}
		asyncSecretHandle.recordDecrypt(commandMap);
		return targets;
	}

	/**
	 * 获取列表中相应的密钥
	 * @param secretManages 集合
	 * @param index 需要获取的模值
	 * @return
	 */
	private SecretManage getSecretManageByIndex(List<SecretManage> secretManages, int index) {
		SecretManage secretManage = new SecretManage();

		for (SecretManage secret : secretManages) {
			if (secret.getSecretIndex() == index) {
				secretManage = secret;
				break;
			}
		}

		return secretManage;
	}

	/**
	 * @param secretManages 集合
	 * @param command 需要获取的密钥的口令值
	 * @return
	 */
	private SecretManage getSecretManageByCommand(List<SecretManage> secretManages, String command) {
		SecretManage secretManage = new SecretManage();

		for (SecretManage secret : secretManages) {
			if (secret.getSecretCommand().equals(command)) {
				secretManage = secret;
				break;
			}
		}
		return secretManage;
	}

	@Override
    public ArrayList<EncryptData> decrypt(List<String> sources, Integer mosaic) {
        ArrayList<EncryptData> targets = new ArrayList<>();
        if(sources==null){
            return targets;
        }
        Map<String, Integer> commandMap = new HashMap(200);
        List<SecretSecurity> securityManagers = PassportUtil.getSecurityCode();
        SecretSecurity secretSecurity = PassportUtil.getMaxSecurityCode(securityManagers);
        List<SecretManage> list = redisToDBService.getPwdByCommand(null, null, null, null);
        Map<String, SecretManage> map = new HashMap<>(200);
        for (SecretManage s : list) {
            map.put(s.getSecretCommand(), s);
        }
        for (String data : sources) {
            EncryptData encryptData = new EncryptData(data, data);
            if (data.length() < 8) {
                targets.add(encryptData);
                continue;
            }
            String command = data.substring(0, 8);
            SecretManage sm = map.get(command);
            if (sm == null) {
                targets.add(encryptData);
                continue;
            }
            String pwd = sm.getSecretPassword();
            if (pwd != null && !pwd.isEmpty() && secretSecurity!=null) {
                String password = secretSecurity.getSafeCode() + pwd;
                String result = DESHelper.decryptData(data.substring(8), password);
                //String result = AESUtil.decrypt(data, password);
                if (result != null && !result.trim().isEmpty()) {
                    if (mosaic != null && mosaic == MosaicType.YES.getIndex()) {
                        if (result.length() == 2) {
                            result = result.substring(0, 1) + "*" ;
                        } else if (result.length() >= 3 && result.length() <= 4) {
                            result = result.substring(0, 1) + "*" + result.substring(result.length()-1, result.length());
                        }
                        else if (result.length() > 4 && result.length() <= 8) {
                            result = result.substring(0, 2) + "***" + result.substring(result.length()-2, result.length());
                        }
                        else if (result.length() > 8 ) {
                            result = result.substring(0, 4) + "*****" + result.substring(result.length()-4, result.length());
                        }
                    }
                    encryptData.setRefData(result);
                }
                if (commandMap.containsKey(command)) {
                    commandMap.put(command, commandMap.get(command) + 1);
                } else {
                    commandMap.put(command, 1);
                }
            }
            targets.add(encryptData);
        }
        asyncSecretHandle.recordDecrypt(commandMap);
        return targets;
    }

}
