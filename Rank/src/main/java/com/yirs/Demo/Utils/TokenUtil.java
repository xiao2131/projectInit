package com.yirs.Demo.Utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yirs.Demo.entity.WxUser;

/* 
* @author MRC 
* @date 2019年4月5日 下午1:14:53 
* @version 1.0 
*/
public class TokenUtil {
	/**
	 * 任务：jwt识别权限
	 * @param user
	 * @param time
	 * @return
	 */

	// 生成token
	public static String getToken(String openid, long time) {

		Date start = new Date();
		Date end = new Date(time);
		String token = "";

		token = JWT.create()
				.withAudience(openid)
				.withIssuedAt(start)
				.withExpiresAt(end)
				.sign(Algorithm.HMAC256(openid));
		return token;
	}

	public static String getTokenUserId() {
		String token = getRequest().getHeader("token");// 从 http 请求头中取出 token
		String userId = JWT.decode(token).getAudience().get(0);
		return userId;
	}

	/**
	 * 获取request
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return requestAttributes == null ? null : requestAttributes.getRequest();
	}
}
