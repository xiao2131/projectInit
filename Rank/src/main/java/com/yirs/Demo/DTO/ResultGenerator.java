package com.yirs.Demo.DTO;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {

	private static final String DEFAULT_SUCCESS_MESSAGE = "OK";

	// 不带数据返回成功信息
	@SuppressWarnings("rawtypes")
	public static Result getSuccessResult() {

		return new Result().setCode(ResultCode.SUCCESS).setMsg(DEFAULT_SUCCESS_MESSAGE);

	}

	// 带数据返回成功信息
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Result<T> getSuccessResult(T data) {

		return new Result().setCode(ResultCode.SUCCESS).setMsg(DEFAULT_SUCCESS_MESSAGE).setData(data);

	}

	// 返回失败信息（带提示码）
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Result getFailResult(String message, int data) {

		return new Result().setCode(ResultCode.FAIL).setMsg(message).setData(data);
	}

	@SuppressWarnings({ "rawtypes" })
	public static Result getFailResult(String errorCode) {

		return new Result().setCode(ResultCode.FAIL).setErrorCode(errorCode);
	}

}
