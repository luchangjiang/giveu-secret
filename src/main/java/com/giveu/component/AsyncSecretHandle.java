package com.giveu.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by fox on 2018/8/16.
 */
@Component
public class AsyncSecretHandle {

	public static Logger logger = LoggerFactory.getLogger(AsyncSecretHandle.class);

	@Autowired
	RedisToDBService redisToDBService;


	@Async("executor")
	public void recordEncrypt(Map<String, Integer> commandMap){
		for (Map.Entry<String, Integer> vo : commandMap.entrySet()) {
			redisToDBService.updateSecretTimes(vo.getKey(), 0, vo.getValue());
		}
		logger.info("Redis encrypt update finish...");
	}
	@Async("executor")
	public void recordDecrypt(Map<String, Integer> commandMap){
		for (Map.Entry<String, Integer> vo : commandMap.entrySet()) {
			redisToDBService.updateSecretTimes(vo.getKey(), vo.getValue(), 0);
		}
		logger.info("Redis decrypt update finish...");
	}

}
