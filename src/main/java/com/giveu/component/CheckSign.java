package com.giveu.component;

import com.alibaba.fastjson.JSONObject;
import com.giveu.common.service.RedisService;
import com.giveu.job.common.util.MD5Util;
import com.giveu.job.common.vo.AppVo;
import com.giveu.util.RedisUtil;
import com.giveu.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 验签组件
 * Created by fox on 2018/9/5.
 */
@Component
public class CheckSign {

	public static Logger logger = LoggerFactory.getLogger(CheckSign.class);

	// 获取app信息的Redis key
	private static final String GIVEU_APP_SECRET_INFO = "GIVEU_APP_SECRET_INFO";

	@Autowired
	RedisUtil redisUtil;
	//@Autowired
	//RedisService redisUtil;

	public Boolean check(HttpServletRequest request) {
		String appSecret = "";
		Object obj= redisUtil.get(GIVEU_APP_SECRET_INFO);
		String appInfoJson = String.valueOf(redisUtil.get(GIVEU_APP_SECRET_INFO));
		List<AppVo> list = JSONObject.parseArray(appInfoJson, AppVo.class);

		Map<String, String> map = new HashMap<>();
		long dt = new Date().getTime();
		String xGiveuSign = request.getHeader("xGiveuSign");
		String xGiveuAppKey = request.getHeader("xGiveuAppKey");
		String xGiveuTimestamp = request.getHeader("xGiveuTimestamp");
		xGiveuTimestamp="1542016080935";
		xGiveuAppKey="giveu-contract-service";
		xGiveuSign="ABCABD80A7E0AC9127BF1A80B9DA7F44";
		Boolean b = StringUtil.isEmptyBatch(xGiveuSign, xGiveuAppKey, xGiveuTimestamp);
		if (b) {
			logger.info("公共参数不能为空...");
			return false;
		}
		long timestamp = Long.parseLong(xGiveuTimestamp);
		if (timestamp > dt || dt - timestamp >= 1000 * 60) {
			logger.info("时间戳有误");
			return false;
		}
		for (AppVo app : list) {
			if (xGiveuAppKey.equals(app.getAppKey())) {
				appSecret = app.getAppSecret();
			}
		}

		map.put("xGiveuSign", xGiveuSign);
		map.put("xGiveuAppKey", xGiveuAppKey);
		map.put("xGiveuTimestamp", xGiveuTimestamp);

		StringBuilder sb = new StringBuilder();
		// 遍历请求参数，判断是否有空字段，且生成部分原始加密字符串
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals("xGiveuSign")) {
				continue;
			}
			sb.append(entry.getKey() + entry.getValue().toUpperCase());
		}
		logger.info(appSecret);
		sb.append(appSecret);
		String originaly = sb.toString();
		String encrypted = MD5Util.sign(originaly);
		logger.info(encrypted);
		if (encrypted.equals(xGiveuSign)) {
			return true;
		}

		return false;

	}

}
