package com.yirs.Demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yirs.Demo.DTO.ResultGenerator;
import com.yirs.Demo.annotation.Authority;
import com.yirs.Demo.service.impl.SchoolServiceImpl;

/**
 * 
 * @author    Yirs
 * @describe  管理员相关接口
 * @Date      2020年7月21日
 */
@RequestMapping("/v1/api/admin")
@RestController
@SuppressWarnings("static-access")
public class AdminController {

	@Autowired
	private SchoolServiceImpl schoolServiceImpl;

	/**
	 * 	更新招生院校信息
	 */
//	@Authority
	@GetMapping("/updateSchoolInfo")
	public Object updateSchoolInfo() {
		if (schoolServiceImpl.upadteSchoolInfo())
			return new ResultGenerator().getSuccessResult();

		return new ResultGenerator().getFailResult("20001");// 更新院校信息失败
	}

}
