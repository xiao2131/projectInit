package cn.yirs.Service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.yirs.Service.CallBackService;
import cn.yirs.Utils.Constant;
import cn.yirs.Utils.NasApi;
import cn.yirs.Utils.UserGroupJson;

@Service
public class CallBackServiceImpl implements CallBackService {

	private static final Logger log2 = LoggerFactory.getLogger("nas账号创建");

	@Autowired
	private UserGroupJson userGroupJson;

	/**
	 * 处理成功事件
	 */
	public Object authorizationEvent(JSONObject json) {

		// 获取分配用户组的字符串
		String nasGroupsStr = json.getString(Constant.NAS_USER_DEPT);
		// 获取分配的用户组名字
		List<String> list = JSONArray.parseArray(nasGroupsStr, String.class);
		List<String> nasGroup = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			Map<String, Object> map = JSON.parseObject(userGroupJson.getFileJson());
			if (!map.isEmpty()) {
				for (String string : list) {
					if (StringUtils.isNotBlank((CharSequence) map.get(string))) {
						nasGroup.add((String) map.get(string));
					}
				}
			} else {
				log2.info("userGroup.json文件格式不匹配。请按照样本格式");
			}
		} else {
			log2.info("获取简道云的nas用户组为空，请管理员确认清楚是否勾选用户组");
		}
		if (!nasGroup.isEmpty()) {
			// 数量
			Integer count = 0;
			Map<String, Object> params = new HashMap<String, Object>();
			// 获取到文件和网络权限
			params = NasApi.getNetAndFileAuthor(params);
			// 存放登录名
			params.put("a_username", json.get(Constant.USERNAME));
			// 存放密码
			params.put("a_passwd", NasApi.encodeToString(json.getString(Constant.PASSWORD)));
			for (int i = 0; i < nasGroup.size(); i++) {
				// 判断是否有,即为多个用户组
				if (nasGroup.get(i).contains(",")) {
					String[] args = nasGroup.get(i).split(",");
					for (int j = 0; j < args.length; j++) {
						params.put("gp" + count++, args[j]);
					}
				} else {
					params.put("gp" + count++, nasGroup.get(i));
				}
			}
			// 存放的用户组数量
			params.put("gp_len", count);
			NasApi.createUser(params);
			log2.info("账号:" + json.getString(Constant.USERNAME) + "已成功创建");
			return "{\"status\": true}";
		}

		return null;
	}

}
