package com.yirs.Demo.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一API响应结果封装
 */
//取掉json数据中值为null的属性字段
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

	// 状态码
	private int code;

	// 消息
	private String msg;

	// 数据
	private T data;

	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public Result<T> setErrorCode(String errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	public Result<T> setCode(ResultCode resultCode) {
		this.code = resultCode.code();
		return this;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Result<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public T getData() {
		return data;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

}
