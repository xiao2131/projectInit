package com.yirs.Demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yirs.Demo.DTO.ResultGenerator;
import com.yirs.Demo.entity.Impl.WxUserImpl;
import com.yirs.Demo.service.WxUserService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yirs
 * @since 2020-07-22
 */
@RequestMapping("/v1/api/wx")
@RestController
@SuppressWarnings("static-access")
public class WxUserController {

	@Autowired
	private WxUserService wxUserService;

	@PostMapping("/login")
	public Object login(WxUserImpl wxUserImpl) {

		if (StringUtils.isNotBlank(wxUserImpl.getCode()))
			return wxUserService.diyVerifyUser(wxUserImpl);

		// 10002 未传入code参数
		return ResultGenerator.getFailResult("10002");

	}

}
