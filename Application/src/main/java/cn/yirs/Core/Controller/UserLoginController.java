package cn.yirs.Core.Controller;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.yirs.Common.Dto.ResultGenerator;
import cn.yirs.Common.Entity.User;
import cn.yirs.Core.Service.UserService;
import cn.yirs.Utils.TokenUtils;

/**
 * @author by yirs
 */
@RestController
@RequestMapping("/users")
public class UserLoginController {

	private Log logger = LogFactory.getLog(UserLoginController.class);

	@Autowired
	private UserService userService;

	// 用户登录
	@PostMapping("/login")
	public Object login(@RequestBody String body) {

		logger.info(body);

		if (!StringUtils.isEmpty(body)) {

			JSONObject json = JSONObject.parseObject(body);

			String username = json.getString("username");

			String password = json.getString("password");

			if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {

				User userVo = userService.getUserByName(username);

				if (userVo != null) {

					if (password.equals(userVo.getPassword())) {

						// 查询出角色列表
						ArrayList<String> authorities = new ArrayList<>();
						authorities.add("ROLE_ADMIN");
						authorities.add("ROLE_USER");

						// jwt官方格式 eg:user-[ROLE-ADMIN,ROLE-USER]
						String subject = userVo.getId() + "-" + userVo.getUsername() + "-" + authorities;

						String accessToken = TokenUtils.getToken(subject,
								TokenUtils.setTokenLifeCycle(Calendar.MINUTE, 120));

						String refreshToken = TokenUtils.getToken(subject,
								TokenUtils.setTokenLifeCycle(Calendar.MONTH, 1));

						return ResultGenerator.getSuccessTokenAndFreshToken(accessToken, refreshToken);
					}
					return ResultGenerator.getFailResult("密码错误,请重新输入密码", 10003);
				}
				return ResultGenerator.getFailResult("用户不存在", 10002);
			}
			return ResultGenerator.getFailResult("用户参数为空", 10001);
		}
		return ResultGenerator.getFailResult("json参数不能为空", 90001);

	}

}
