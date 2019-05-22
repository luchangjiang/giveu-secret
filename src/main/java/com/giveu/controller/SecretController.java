/**
 * 
 */
package com.giveu.controller;

import com.giveu.common.ResponseCode;
import com.giveu.component.CheckSign;
import com.giveu.entity.EncryptData;
import com.giveu.entity.ReqRefData;
import com.giveu.entity.ResponseModel;
import com.giveu.service.SecretService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 529017
 * 加解密服务
 */
@RestController
@RequestMapping(value="/Api/Security")
@CrossOrigin
public class SecretController {
	private static Logger logger = LoggerFactory.getLogger(SecretController.class);

	@Autowired
	SecretService secretService;

	@Autowired
	CheckSign checkSign;

	/**
	 * @return 加密后的数据
	 * @data 待加密数据
	 */
	@RequestMapping(value = "/Encrypt", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseModel<EncryptData> encrypt(@RequestBody ReqRefData data, HttpServletRequest request) {

		ResponseModel<EncryptData> responseModel = new ResponseModel();

//		if (!checkSign.check(request)) {
//			responseModel.setMessage(ResponseCode.FORBIDDEN.getName());
//			responseModel.setCode(ResponseCode.FORBIDDEN.getIndex());
//			responseModel.setResult(false);
//			responseModel.setData(null);
//			return responseModel;
//		}

		try {
			responseModel.setMessage(ResponseCode.OK.getName());
			responseModel.setCode(ResponseCode.OK.getIndex());

			List<String> list = data.getRefData();
			responseModel.setData(secretService.encrypt(list));
			responseModel.setResult(true);
			logger.info("加密成功");
		} catch (Exception e) {
			responseModel.setMessage(e.getMessage());
			responseModel.setCode(ResponseCode.ERROR.getIndex());
			responseModel.setResult(false);
			logger.info("加密异常：" + e.getMessage() + e.getStackTrace());
		}
		return responseModel;

	}

	/**
	 * @return 返回解密后的数据
	 * @data 待解密数据
	 */
	@RequestMapping(value = "/Decrypt", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseModel<EncryptData> decrypt(@RequestBody ReqRefData data, HttpServletRequest request) {
		ResponseModel<EncryptData> responseModel = new ResponseModel();

		if (!checkSign.check(request)) {
			responseModel.setMessage(ResponseCode.FORBIDDEN.getName());
			responseModel.setCode(ResponseCode.FORBIDDEN.getIndex());
			responseModel.setResult(false);
			return responseModel;
		}
		try {
			responseModel.setMessage(ResponseCode.OK.getName());
			responseModel.setCode(ResponseCode.OK.getIndex());
			responseModel.setResult(true);
			List<String> list = data.getRefData();
			responseModel.setData(secretService.decrypt(list, data.getMosaic()));
			logger.info("解密成功");
		} catch (Exception e) {
			responseModel.setMessage(e.getMessage());
			responseModel.setResult(false);
			responseModel.setData(null);
			responseModel.setCode(ResponseCode.ERROR.getIndex());
			logger.info("加密异常：" + e.getMessage() + e.getStackTrace());
		}
		return responseModel;
	}
}
