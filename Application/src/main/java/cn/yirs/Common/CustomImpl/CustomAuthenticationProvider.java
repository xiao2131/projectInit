package cn.yirs.Common.CustomImpl;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义身份认证验证组件
 *
 * @author zhaoxinguo on 2017/9/12.
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private Log log = LogFactory.getLog(CustomAuthenticationProvider.class);

	private UserDetailsService userDetailsService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public CustomAuthenticationProvider(UserDetailsService userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		log.info("自定义认证流程");

		// 获取认证的用户名 & 密码
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		// 认证逻辑
		UserDetails userDetails = userDetailsService.loadUserByUsername(name);
		if (null != userDetails) {
			if (bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
				// 这里设置权限和角色
				ArrayList<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
				// 生成令牌 这里令牌里面存入了:name,password,authorities, 当然你也可以放其他内容
				Authentication auth = new UsernamePasswordAuthenticationToken(name, password, authorities);

				log.info("自定义认证流程 结束");

				return auth;
			} else {
				throw new BadCredentialsException("密码错误");
			}

		} else {
			throw new UsernameNotFoundException("用户不存在");
		}

	}

	/**
	 * 是否可以提供输入类型的认证服务
	 * 
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
