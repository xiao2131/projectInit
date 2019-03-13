package cn.yirs.Common.Dto;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
	
	private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

	//不带数据返回成功信息
	public static Result getSuccessResult() {

		return new Result().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE);

	}
	
	//带数据返回成功信息
	public static <T> Result<T> getSuccessResult(T data) {

		return new Result().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(data);

	}
	
	//返回失败信息
	public static Result getFailResult(String message, int data) {

		return new Result().setCode(ResultCode.FAIL).setMessage(message).setData(data);
	}
	
//	登录成功返回access_token和refresh_token
	public static Token getSuccessTokenAndFreshToken(String accessToken,String refreshToken) {

		return new Token().
				setCode(ResultCode.SUCCESS).
				setMessage(DEFAULT_SUCCESS_MESSAGE).
				setAccess_token(accessToken).
				setRefresh_token(refreshToken);
	}
	
	

}
