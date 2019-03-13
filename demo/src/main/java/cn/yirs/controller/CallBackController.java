package cn.yirs.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.yirs.Service.CallBackService;
import cn.yirs.Utils.Constant;

@RestController
@RequestMapping("api")
public class CallBackController {

	@Autowired
	private CallBackService callBackService;

	private static final Logger log2 = LoggerFactory.getLogger("审批结果");

	@RequestMapping("callback")
	public Object callBack(@RequestBody String body) {
		try {
			if (StringUtils.isNotBlank(body)) {
				JSONObject json = JSONObject.parseObject(body);
				if (json != null) {
					String op = json.getString("op");
					// 是修改推送就执行
					if (op.equals("data_update")) {
						// 得到data数据
						JSONObject resultJson = JSONObject.parseObject(json.getString("data"));
						if (resultJson != null) {
							// 获取审批结果
							String result = resultJson.getString(Constant.APPROVAL_RESULT);
							if (StringUtils.isNotBlank(result)) {
								if (result.equals(Constant.SUCCESS)) {
									// 执行通过事件
									return callBackService.authorizationEvent(resultJson);
								} else {
									log2.info("管理员驳回");
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

}
