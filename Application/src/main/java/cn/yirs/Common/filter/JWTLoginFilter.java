package cn.yirs.Common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.yirs.Common.Dto.ResultGenerator;
import cn.yirs.Common.Entity.User;
import cn.yirs.Utils.TokenUtils;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法 attemptAuthentication
 * ：接收并解析用户凭证。 successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 * 
 * 此类继承UsernamePasswordAuthenticationFilter，在进行用户名和密码匹配的时候会调用此类
 * 登录成功就会访问successfulAuthentication
 * 
 * @author by yirs 2019年1月17日17:01:58(重新理解)
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTLoginFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	// 接收并解析用户凭证
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {

		logger.info("解析用户凭证");

		try {
			User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		// builder the token
		logger.info("用户登录成功");
		try {

			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

			// 定义存放角色集合的对象
			List<String> roleList = new ArrayList<String>();
			for (GrantedAuthority grantedAuthority : authorities) {
				roleList.add(grantedAuthority.getAuthority());
			}

			// 登录成功 返回访问token 生命周期为1分钟
			String accessToken = TokenUtils.getToken(auth, roleList, TokenUtils.setTokenLifeCycle(Calendar.MINUTE, 1));

			// 刷新token 生命周期为一个月
			String refreshToken = TokenUtils.getToken(auth, roleList,
					TokenUtils.setTokenLifeCycle(Calendar.DAY_OF_MONTH, 1));

			response.setCharacterEncoding("UTF-8");

			response.setContentType("application/json;charset=UTF8");

			PrintWriter writer = response.getWriter();

			writer.write(JSON.toJSONString(ResultGenerator.getSuccessTokenAndFreshToken(accessToken, refreshToken)));

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
//		SecurityContextHolder.clearContext();

		logger.info("用户登录失败");

		response.setCharacterEncoding("UTF-8");

		response.setContentType("application/json;charset=UTF8");

		PrintWriter writer = response.getWriter();

		writer.write(JSON.toJSONString(ResultGenerator.getFailResult("登录失败~请检查用户名或密码是否错误", 10001)));

		writer.close();

//		if (logger.isDebugEnabled()) {
//			logger.debug("Authentication request failed: " + failed.toString(), failed);
//			logger.debug("Updated SecurityContextHolder to contain null Authentication");
//			logger.debug("Delegating to authentication failure handler " + failureHandler);
//		}
//
//		rememberMeServices.loginFail(request, response);
//
//		failureHandler.onAuthenticationFailure(request, response, failed);
	}

}
