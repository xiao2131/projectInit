package com.yirs.Demo.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.yirs.Demo.Exception.TokenErrorException;
import com.yirs.Demo.Utils.RedisUtil;
import com.yirs.Demo.annotation.Authority;
import com.yirs.Demo.annotation.PassToken;
import com.yirs.Demo.config.TokenConfig;
import com.yirs.Demo.entity.WxUser;
import com.yirs.Demo.service.WxUserService;

/**
 * @author jinbin
 * @date 2018-07-08 20:41
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private TokenConfig tokenConfig;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object object) throws Exception {

		String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token

		if (token != null && (token.contains("Undefined") || token.contains("null"))) {
			token = null;
		}

		// 如果不是映射到方法直接通过
		if (!(object instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) object;
		Method method = handlerMethod.getMethod();

		// 检查是否有passtoken注释，有则跳过认证
		if (method.isAnnotationPresent(PassToken.class)) {
			PassToken passToken = method.getAnnotation(PassToken.class);
			if (passToken.required()) {
				return true;
			}
		}
		// 检查有没有需要用户权限的注解
		if (method.isAnnotationPresent(Authority.class)) {
			Authority userLoginToken = method.getAnnotation(Authority.class);
			if (userLoginToken.required()) {

				// 执行认证
				if (token == null) {
					// 未登陆，请重新登录
					throw new TokenErrorException("90001");
				}

				// Redis判断Token是否过期(相当于判断是否存在)，无过期就刷新缓存 直接放行
				if (redisUtil.getExpire(token) >= 0) {
					redisUtil.expire(token, tokenConfig.getExpireTime() * 60);
					return true;
				}

				// 获取 token 中的 user id
				String openId;
				try {
					openId = JWT.decode(token).getAudience().get(0);
				} catch (JWTDecodeException j) {
					// 无效凭证 (无法解密token的userid)
					throw new TokenErrorException("90002");
				}

				WxUser user = wxUserService.getById(openId);

				if (user == null) {
					// 凭证非法构成 (Token的userid不存在)
					throw new TokenErrorException("90003");
				}

				// 验证 token
				JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getOpenid())).build();
				try {
					jwtVerifier.verify(token);
				} catch (JWTVerificationException e) {
					// 凭证过期
					throw new TokenErrorException("90004");
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {

	}
}
