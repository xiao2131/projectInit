package cn.yirs.Common.Dto;

import com.alibaba.fastjson.JSON;

public class Token {

	// 状态码
	private int code;

	// 消息
	private String message;

	// 访问token
	private String access_token;

	// 刷新token
	private String refresh_token;

	public String getAccess_token() {
		return access_token;
	}

	public Token setAccess_token(String access_token) {
		this.access_token = access_token;
		return this;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public Token setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
		return this;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public int getCode() {
		return code;
	}

	public Token setCode(ResultCode resultCode) {
		this.code = resultCode.code();
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Token setMessage(String message) {
		this.message = message;
		return this;
	}

}
