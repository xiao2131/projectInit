package cn.yirs.Service;

import com.alibaba.fastjson.JSONObject;

public interface CallBackService {
	
	/**
	 * 处理授权成功
	 */
	public Object authorizationEvent(JSONObject json);

}
