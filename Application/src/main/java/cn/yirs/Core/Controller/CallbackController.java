package cn.yirs.Core.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.dingtalk.oapi.lib.aes.DingTalkEncryptException;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import com.dingtalk.oapi.lib.aes.Utils;

import cn.yirs.Core.Service.CallbackService;
import cn.yirs.Core.Service.Impl.CallbackServiceImpl;
import cn.yirs.Utils.CallBackEvent;
import cn.yirs.Utils.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 通讯录回调事件
 */
@RestController
public class CallbackController {

	private static final Logger bizLogger = LoggerFactory.getLogger("CallBackEvent");

	private static final Logger mainLogger = LoggerFactory.getLogger(CallbackController.class);

	@Autowired
	private CallbackService callbackService;

	/**
	 * 
	 * @param signature 签名
	 * @param timestamp 时间戳
	 * @param nonce     随机文本
	 * @param json      请求包体
	 * @return 返回加密后的success
	 */
	@RequestMapping(value = "callback", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> callback(@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "timestamp", required = false) String timestamp,
			@RequestParam(value = "nonce", required = false) String nonce,
			@RequestBody(required = false) JSONObject json) {

		String params = " signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " json:" + json;
		try {

			DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(Constant.TOKEN, Constant.ENCODING_AES_KEY,
					Constant.SUITE_KEY);

			// 从post请求的body中获取回调信息的加密数据进行解密处理
			String encryptMsg = json.getString("encrypt");

			String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encryptMsg);

			String result = callbackService.BookListener(plainText);

			// 成功就返回success字段给钉钉服务器
			if (result.equals(Constant.CALLBACK_RESPONSE_SUCCESS)) {
				return dingTalkEncryptor.getEncryptedMap(Constant.CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(),
						Utils.getRandomStr(8));
			}

			// 失败就发送失败 到时候会重新发送
			return dingTalkEncryptor.getEncryptedMap(Constant.CALLBACK_RESPONSE_FAIL, System.currentTimeMillis(),
					Utils.getRandomStr(8));

			// 返回success的加密信息表示回调处理成功

		} catch (Exception e) {
			// 失败的情况，应用的开发者应该通过告警感知，并干预修复
			mainLogger.error("process callback failed！" + params, e);
			return null;
		}

	}
}