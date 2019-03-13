package cn.yirs.Common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import cn.yirs.Common.CustomImpl.GrantedAuthorityImpl;
import cn.yirs.Common.constant.ConstantKey;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * 自定义JWT认证过滤器 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 * 
 * @author zhaoxinguo on 2017/9/13.
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	/**
	 * 认证token，根据token获取用户信息和权限
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		logger.info("开始认证token");

		String header = request.getHeader("token");

		if (StringUtils.isEmpty(header)) {

			// 非法访问
			request.setAttribute("errCode", "40000");

			chain.doFilter(request, response);

			return;

		}

		// 取到个人信息和权限
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		// 设置个人信息和权限 这样访问controller才能进行权限检测
		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(request, response);

	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		long start = System.currentTimeMillis();

		String token = request.getHeader("token");

		String user = null;

		try {
			user = Jwts.parser().setSigningKey(ConstantKey.SIGNING_KEY).parseClaimsJws(token).getBody().getSubject();

			long end = System.currentTimeMillis();

			logger.info("执行时间: {}", (end - start) + " 毫秒");

			if (user != null) {

				String[] split = user.split("-")[2].replaceAll("(\\[)|(\\])", "").split(",");

				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

				for (int i = 0; i < split.length; i++) {

					authorities.add(new GrantedAuthorityImpl(split[i].trim()));
				}

				logger.info("开始认证token结束");

				return new UsernamePasswordAuthenticationToken(user, null, authorities);
			}

		} catch (ExpiredJwtException e) {
			logger.error(request.getRemoteAddr() + "Token已过期");
			request.setAttribute("errCode", "40001");

		} catch (UnsupportedJwtException e) {
			logger.error(request.getRemoteAddr() + "Token格式错误 ");
			request.setAttribute("errCode", "40002");

		} catch (MalformedJwtException e) {
			logger.error(request.getRemoteAddr() + "Token没有被正确构造");
			request.setAttribute("errCode", "40003");

		} catch (SignatureException e) {
			logger.error(request.getRemoteAddr() + "签名失败");
			request.setAttribute("errCode", "40004");

		} catch (IllegalArgumentException e) {
			logger.error(request.getRemoteAddr() + "非法参数异常");
			request.setAttribute("errCode", "40005");
		}

		return null;

	}

}
